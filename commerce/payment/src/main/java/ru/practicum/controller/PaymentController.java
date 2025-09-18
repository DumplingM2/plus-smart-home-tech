package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/product-cost")
    public ResponseEntity<Double> calculateProductCost(@RequestBody PaymentDto paymentDto) {
        Double cost = paymentService.calculateProductCost(paymentDto);
        return ResponseEntity.ok(cost);
    }

    @PostMapping("/total-cost")
    public ResponseEntity<Double> calculateTotalCost(@RequestBody PaymentDto paymentDto) {
        Double totalCost = paymentService.calculateTotalCost(paymentDto);
        return ResponseEntity.ok(totalCost);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto payment = paymentService.createPayment(paymentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping("/{paymentId}/success")
    public ResponseEntity<PaymentDto> markPaymentAsSuccess(@PathVariable UUID paymentId) {
        PaymentDto payment = paymentService.markPaymentAsSuccess(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{paymentId}/failed")
    public ResponseEntity<PaymentDto> markPaymentAsFailed(@PathVariable UUID paymentId) {
        PaymentDto payment = paymentService.markPaymentAsFailed(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID paymentId) {
        PaymentDto payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }
}