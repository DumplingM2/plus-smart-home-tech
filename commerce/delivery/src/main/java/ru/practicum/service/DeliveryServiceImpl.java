package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.enums.delivery.DeliveryState;
import ru.practicum.exceptions.NoDeliveryFoundException;
import ru.practicum.mapper.DeliveryMapper;
import ru.practicum.model.Delivery;
import ru.practicum.repository.DeliveryRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Value("${delivery.base_cost:5.0}")
    private Double baseCost;

    @Value("${delivery.warehouse_address_ratio:2}")
    private Double warehouseAddressRatio;

    @Value("${delivery.fragile_ratio:0.2}")
    private Double fragileRatio;

    @Value("${delivery.weight_ratio:0.3}")
    private Double weightRatio;

    @Value("${delivery.volume_ratio:0.2}")
    private Double volumeRatio;

    @Value("${delivery.delivery_address_ratio:0.2}")
    private Double deliveryAddressRatio;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        log.info("Creating new delivery for order: {}", deliveryDto.getOrderId());
        
        Delivery delivery = Delivery.builder()
                .orderId(deliveryDto.getOrderId())
                .totalVolume(deliveryDto.getTotalVolume())
                .totalWeight(deliveryDto.getTotalWeight())
                .isFragile(deliveryDto.getIsFragile())
                .deliveryAddress(deliveryDto.getDeliveryAddress())
                .warehouseAddress(deliveryDto.getWarehouseAddress())
                .state(DeliveryState.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery created with ID: {}", savedDelivery.getId());
        
        return DeliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    public Double calculateDeliveryCost(DeliveryDto deliveryDto) {
        log.info("Calculating delivery cost");
        
        Double cost = baseCost;
        
        // Apply warehouse address ratio
        if (deliveryDto.getWarehouseAddress() != null && 
            deliveryDto.getWarehouseAddress().contains("ADDRESS_2")) {
            cost = cost * warehouseAddressRatio;
        }
        
        // Add base cost
        cost = cost + baseCost;
        
        // Apply fragile ratio
        if (deliveryDto.getIsFragile() != null && deliveryDto.getIsFragile()) {
            cost = cost + (cost * fragileRatio);
        }
        
        // Apply weight ratio
        if (deliveryDto.getTotalWeight() != null) {
            cost = cost + (deliveryDto.getTotalWeight() * weightRatio);
        }
        
        // Apply volume ratio
        if (deliveryDto.getTotalVolume() != null) {
            cost = cost + (deliveryDto.getTotalVolume() * volumeRatio);
        }
        
        // Apply delivery address ratio (simplified logic)
        if (deliveryDto.getDeliveryAddress() != null && 
            deliveryDto.getWarehouseAddress() != null &&
            !deliveryDto.getDeliveryAddress().equals(deliveryDto.getWarehouseAddress())) {
            cost = cost + (cost * deliveryAddressRatio);
        }
        
        log.info("Delivery cost calculated: {}", cost);
        return cost;
    }

    @Override
    @Transactional
    public DeliveryDto acceptDelivery(UUID deliveryId) {
        log.info("Accepting delivery for ID: {}", deliveryId);
        
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found with ID: " + deliveryId));
        
        delivery.setState(DeliveryState.IN_PROGRESS);
        delivery.setUpdatedAt(LocalDateTime.now());
        
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery accepted for ID: {}", savedDelivery.getId());
        
        return DeliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    @Transactional
    public DeliveryDto markAsDelivered(UUID deliveryId) {
        log.info("Marking delivery as delivered for ID: {}", deliveryId);
        
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found with ID: " + deliveryId));
        
        delivery.setState(DeliveryState.DELIVERED);
        delivery.setUpdatedAt(LocalDateTime.now());
        
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery marked as delivered for ID: {}", savedDelivery.getId());
        
        return DeliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    @Transactional
    public DeliveryDto markAsFailed(UUID deliveryId) {
        log.info("Marking delivery as failed for ID: {}", deliveryId);
        
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found with ID: " + deliveryId));
        
        delivery.setState(DeliveryState.FAILED);
        delivery.setUpdatedAt(LocalDateTime.now());
        
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery marked as failed for ID: {}", savedDelivery.getId());
        
        return DeliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    public DeliveryDto getDelivery(UUID deliveryId) {
        log.info("Getting delivery for ID: {}", deliveryId);
        
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found with ID: " + deliveryId));
        
        return DeliveryMapper.toDeliveryDto(delivery);
    }
}