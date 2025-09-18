package ru.practicum.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .username(order.getUsername())
                .shoppingCartId(order.getShoppingCartId())
                .deliveryId(order.getDeliveryId())
                .paymentId(order.getPaymentId())
                .state(order.getState())
                .products(order.getProducts())
                .totalVolume(order.getTotalVolume())
                .totalWeight(order.getTotalWeight())
                .isFragile(order.getIsFragile())
                .totalPrice(order.getTotalPrice())
                .productsPrice(order.getProductsPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static Order toOrder(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .username(orderDto.getUsername())
                .shoppingCartId(orderDto.getShoppingCartId())
                .deliveryId(orderDto.getDeliveryId())
                .paymentId(orderDto.getPaymentId())
                .state(orderDto.getState())
                .products(orderDto.getProducts())
                .totalVolume(orderDto.getTotalVolume())
                .totalWeight(orderDto.getTotalWeight())
                .isFragile(orderDto.getIsFragile())
                .totalPrice(orderDto.getTotalPrice())
                .productsPrice(orderDto.getProductsPrice())
                .deliveryPrice(orderDto.getDeliveryPrice())
                .createdAt(orderDto.getCreatedAt())
                .updatedAt(orderDto.getUpdatedAt())
                .build();
    }

    public static List<OrderDto> toOrderDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    public static Page<OrderDto> toOrderDtoPage(Page<Order> orderPage) {
        List<OrderDto> orderDtos = toOrderDtoList(orderPage.getContent());
        return new PageImpl<>(orderDtos, orderPage.getPageable(), orderPage.getTotalElements());
    }
}