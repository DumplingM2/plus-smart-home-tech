package ru.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.practicum.enums.delivery.DeliveryState;
import ru.practicum.enums.order.OrderState;
import ru.practicum.exceptions.NoDeliveryFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.feign_client.OrderClient;
import ru.practicum.feign_client.WarehouseClient;
import ru.practicum.feign_client.exception.warehouse.OrderBookingNotFoundException;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.DeliveryMapper;
import ru.practicum.model.Address;
import ru.practicum.model.Delivery;
import ru.practicum.repository.AddressRepository;
import ru.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Value("${delivery.base_cost}")
    private Double baseCost;
    @Value("${delivery.warehouse_address_ratio}")
    private Integer warehouseAddressRatio;
    @Value("${delivery.fragile_ratio}")
    private Double fragileRatio;
    @Value("${delivery.weight_ratio}")
    private Double weightRatio;
    @Value("${delivery.volume_ratio}")
    private Double volumeRatio;
    @Value("${delivery.delivery_address_ratio}")
    private Double deliveryAddressRatio;

    private static final String FIRST_ADDRESS = "ADDRESS_1";
    private static final String SECOND_ADDRESS = "ADDRESS_2";

    @Override
    @Transactional
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        log.info("Создаём новую доставку для заказа orderId = {}", deliveryDto.getOrderId());
        
        Address fromAddress = addressRepository.save(AddressMapper.mapToAddress(deliveryDto.getFromAddress()));
        Address toAddress = addressRepository.save(AddressMapper.mapToAddress(deliveryDto.getToAddress()));

        log.info("Создаём новую доставку для заказа orderId = {} из склада по адресу {} до заказчика по адресу {}", 
                deliveryDto.getOrderId(), fromAddress, toAddress);
        
        Delivery savedDelivery = deliveryRepository.save(DeliveryMapper.mapToDelivery(deliveryDto, fromAddress, toAddress));
        log.info("Успешно создана доставка с deliveryId = {} для заказа orderId = {}", 
                savedDelivery.getDeliveryId(), deliveryDto.getOrderId());
        
        return DeliveryMapper.mapToDto(savedDelivery);
    }

    @Override
    @Transactional
    public Double calculateDeliveryCost(OrderDto orderDto) {
        UUID deliveryId = orderDto.getDeliveryId();
        if (deliveryId == null) {
            throw new ValidationException("Не задан deliveryId");
        }

        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryWeight(orderDto.getDeliveryWeight());
        delivery.setDeliveryVolume(orderDto.getDeliveryVolume());
        delivery.setFragile(orderDto.getFragile());

        log.info("Начинаем расчет стоимости доставки для заказа orderId = {}, deliveryId = {}", 
                orderDto.getOrderId(), deliveryId);
        log.info("Входные параметры заказа orderId = {}: вес = {}, объем = {}, хрупкое = {}", 
                orderDto.getOrderId(), orderDto.getDeliveryWeight(), orderDto.getDeliveryVolume(), orderDto.getFragile());
        
        double result = baseCost;
        log.info("Базовая стоимость для заказа orderId = {}: {}", orderDto.getOrderId(), result);
        
        AddressDto warehouseAddress = AddressMapper.mapToDto(delivery.getFromAddress());
        log.info("Адрес склада для заказа orderId = {}: страна = {}, город = {}, улица = {}", 
                orderDto.getOrderId(), warehouseAddress.getCountry(), warehouseAddress.getCity(), warehouseAddress.getStreet());
        
        if (warehouseAddress.getStreet().equals(SECOND_ADDRESS) && warehouseAddress.getCity().equals(SECOND_ADDRESS) &&
                warehouseAddress.getCountry().equals(SECOND_ADDRESS)) {
            double warehouseCost = result * warehouseAddressRatio;
            result = result + warehouseCost;
            log.info("Применен коэффициент склада для заказа orderId = {}: коэффициент = {}, добавка = {}, итого = {}", 
                    orderDto.getOrderId(), warehouseAddressRatio, warehouseCost, result);
        } else if (warehouseAddress.getStreet().equals(FIRST_ADDRESS) &&
                warehouseAddress.getCity().equals(FIRST_ADDRESS) &&
                warehouseAddress.getCountry().equals(FIRST_ADDRESS)) {
            result += result;
            log.info("Удвоена стоимость для первого адреса склада для заказа orderId = {}: итого = {}", 
                    orderDto.getOrderId(), result);
        }
        
        if (orderDto.getFragile()) {
            double fragileCost = result * fragileRatio;
            result = result + fragileCost;
            log.info("Применен коэффициент хрупкости для заказа orderId = {}: коэффициент = {}, добавка = {}, итого = {}", 
                    orderDto.getOrderId(), fragileRatio, fragileCost, result);
        }
        
        double weightCost = orderDto.getDeliveryWeight() * weightRatio;
        result = result + weightCost;
        log.info("Добавлена стоимость за вес для заказа orderId = {}: вес = {}, коэффициент = {}, стоимость = {}, итого = {}", 
                orderDto.getOrderId(), orderDto.getDeliveryWeight(), weightRatio, weightCost, result);
        
        double volumeCost = orderDto.getDeliveryVolume() * volumeRatio;
        result = result + volumeCost;
        log.info("Добавлена стоимость за объем для заказа orderId = {}: объем = {}, коэффициент = {}, стоимость = {}, итого = {}", 
                orderDto.getOrderId(), orderDto.getDeliveryVolume(), volumeRatio, volumeCost, result);
        
        Address deliveryAddress = delivery.getToAddress();
        log.info("Адрес доставки для заказа orderId = {}: страна = {}, город = {}, улица = {}", 
                orderDto.getOrderId(), deliveryAddress.getCountry(), deliveryAddress.getCity(), deliveryAddress.getStreet());
        
        if (!deliveryAddress.getStreet().equals(warehouseAddress.getStreet()) &&
                !deliveryAddress.getCity().equals(warehouseAddress.getCity()) &&
                !deliveryAddress.getCountry().equals(warehouseAddress.getCountry())) {
            double deliveryAddressCost = result * deliveryAddressRatio;
            result = result + deliveryAddressCost;
            log.info("Применен коэффициент адреса доставки для заказа orderId = {}: коэффициент = {}, добавка = {}, итого = {}", 
                    orderDto.getOrderId(), deliveryAddressRatio, deliveryAddressCost, result);
        } else {
            log.info("Адрес доставки совпадает с адресом склада для заказа orderId = {}, коэффициент не применен", 
                    orderDto.getOrderId());
        }
        
        log.info("Итоговая стоимость доставки для заказа orderId = {}: {}", orderDto.getOrderId(), result);

        return result;
    }

    @Override
    @Transactional
    public void sendProductsToDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);

        log.info("Отправляем запрос на склад для передачи товаров в доставку для заказа orderId = {}, deliveryId = {}", 
                delivery.getOrderId(), deliveryId);
        try {
            warehouseClient.shipProductsToDelivery(ShippedToDeliveryRequest.builder()
                    .deliveryId(deliveryId)
                    .orderId(delivery.getOrderId())
                    .build());
            log.info("Успешно отправлен запрос на склад для заказа orderId = {}", delivery.getOrderId());
        } catch (FeignException e) {
            log.error("Ошибка при отправке запроса на склад для заказа orderId = {}, deliveryId = {}: статус = {}, сообщение = {}", 
                    delivery.getOrderId(), deliveryId, e.status(), e.getMessage());
            if (e.status() == 404) {
                throw new OrderBookingNotFoundException(e.getMessage());
            } else {
                throw e;
            }
        }

        log.info("Меняем статус доставки на {} для заказа orderId = {}, deliveryId = {}", 
                DeliveryState.IN_PROGRESS, delivery.getOrderId(), deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
    }

    @Override
    @Transactional
    public void changeStateToDelivered(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);

        log.info("Меняем статус доставки на {} для заказа orderId = {}, deliveryId = {}", 
                DeliveryState.DELIVERED, delivery.getOrderId(), deliveryId);
        delivery.setState(DeliveryState.DELIVERED);

        log.info("Отправляем запрос на изменение статуса заказа на {} для заказа orderId = {}", 
                OrderState.DELIVERED, delivery.getOrderId());
        orderClient.sendOrderToDelivery(delivery.getOrderId());
        log.info("Успешно отправлен запрос на изменение статуса заказа для orderId = {}", delivery.getOrderId());
    }

    @Override
    public void changeStateToFailed(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);

        log.info("Меняем статус доставки на {} для заказа orderId = {}, deliveryId = {}", 
                DeliveryState.FAILED, delivery.getOrderId(), deliveryId);
        delivery.setState(DeliveryState.FAILED);

        log.info("Отправляем запрос на изменение статуса заказа на {} для заказа orderId = {}", 
                OrderState.DELIVERY_FAILED, delivery.getOrderId());
        orderClient.changeStateToDeliveryFailed(delivery.getOrderId());
        log.info("Успешно отправлен запрос на изменение статуса заказа на неудачную доставку для orderId = {}", 
                delivery.getOrderId());
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставки с id = %s не существует"
                        .formatted(deliveryId)));
    }
}