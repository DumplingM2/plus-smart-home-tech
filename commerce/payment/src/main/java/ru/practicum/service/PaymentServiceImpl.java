package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.enums.payment.PaymentState;
import ru.practicum.exceptions.NoPaymentFoundException;
import ru.practicum.mapper.PaymentMapper;
import ru.practicum.model.Payment;
import ru.practicum.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Double calculateProductCost(PaymentDto paymentDto) {
        log.info("Calculating product cost for payment");
        
        // TODO: Implement product cost calculation logic
        // This should call shopping-store service to get product prices
        Double productCost = 100.0; // Placeholder
        
        log.info("Product cost calculated: {}", productCost);
        return productCost;
    }

    @Override
    public Double calculateTotalCost(PaymentDto paymentDto) {
        log.info("Calculating total cost for payment");
        
        // TODO: Implement total cost calculation logic
        // This should include VAT (10%) and delivery cost
        Double productCost = paymentDto.getProductCost() != null ? paymentDto.getProductCost() : 100.0;
        Double deliveryCost = paymentDto.getDeliveryCost() != null ? paymentDto.getDeliveryCost() : 50.0;
        
        Double vat = productCost * 0.1; // 10% VAT
        Double totalCost = productCost + vat + deliveryCost;
        
        log.info("Total cost calculated: {}", totalCost);
        return totalCost;
    }

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) {
        log.info("Creating new payment");
        
        Payment payment = Payment.builder()
                .orderId(paymentDto.getOrderId())
                .productCost(paymentDto.getProductCost())
                .deliveryCost(paymentDto.getDeliveryCost())
                .totalCost(paymentDto.getTotalCost())
                .state(PaymentState.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", savedPayment.getId());
        
        return PaymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentDto markPaymentAsSuccess(UUID paymentId) {
        log.info("Marking payment as success for ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Payment not found with ID: " + paymentId));
        
        payment.setState(PaymentState.SUCCESS);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment marked as success for ID: {}", savedPayment.getId());
        
        return PaymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentDto markPaymentAsFailed(UUID paymentId) {
        log.info("Marking payment as failed for ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Payment not found with ID: " + paymentId));
        
        payment.setState(PaymentState.FAILED);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment marked as failed for ID: {}", savedPayment.getId());
        
        return PaymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    public PaymentDto getPayment(UUID paymentId) {
        log.info("Getting payment for ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Payment not found with ID: " + paymentId));
        
        return PaymentMapper.toPaymentDto(payment);
    }
}