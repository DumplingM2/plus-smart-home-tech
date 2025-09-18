package ru.practicum.mapper;

import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.model.Payment;

public class PaymentMapper {

    public static PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .productCost(payment.getProductCost())
                .deliveryCost(payment.getDeliveryCost())
                .totalCost(payment.getTotalCost())
                .state(payment.getState())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public static Payment toPayment(PaymentDto paymentDto) {
        return Payment.builder()
                .id(paymentDto.getId())
                .orderId(paymentDto.getOrderId())
                .productCost(paymentDto.getProductCost())
                .deliveryCost(paymentDto.getDeliveryCost())
                .totalCost(paymentDto.getTotalCost())
                .state(paymentDto.getState())
                .createdAt(paymentDto.getCreatedAt())
                .updatedAt(paymentDto.getUpdatedAt())
                .build();
    }
}