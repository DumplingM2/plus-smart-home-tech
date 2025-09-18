package ru.practicum.mapper;

import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.model.Delivery;

public class DeliveryMapper {

    public static DeliveryDto toDeliveryDto(Delivery delivery) {
        return DeliveryDto.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .totalVolume(delivery.getTotalVolume())
                .totalWeight(delivery.getTotalWeight())
                .isFragile(delivery.getIsFragile())
                .deliveryAddress(delivery.getDeliveryAddress())
                .warehouseAddress(delivery.getWarehouseAddress())
                .state(delivery.getState())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }

    public static Delivery toDelivery(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .id(deliveryDto.getId())
                .orderId(deliveryDto.getOrderId())
                .totalVolume(deliveryDto.getTotalVolume())
                .totalWeight(deliveryDto.getTotalWeight())
                .isFragile(deliveryDto.getIsFragile())
                .deliveryAddress(deliveryDto.getDeliveryAddress())
                .warehouseAddress(deliveryDto.getWarehouseAddress())
                .state(deliveryDto.getState())
                .createdAt(deliveryDto.getCreatedAt())
                .updatedAt(deliveryDto.getUpdatedAt())
                .build();
    }
}