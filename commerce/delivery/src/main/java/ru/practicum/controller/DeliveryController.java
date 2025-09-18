package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody DeliveryDto deliveryDto) {
        DeliveryDto delivery = deliveryService.createDelivery(deliveryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
    }

    @PostMapping("/cost")
    public ResponseEntity<Double> calculateDeliveryCost(@RequestBody DeliveryDto deliveryDto) {
        Double cost = deliveryService.calculateDeliveryCost(deliveryDto);
        return ResponseEntity.ok(cost);
    }

    @PostMapping("/{deliveryId}/accept")
    public ResponseEntity<DeliveryDto> acceptDelivery(@PathVariable UUID deliveryId) {
        DeliveryDto delivery = deliveryService.acceptDelivery(deliveryId);
        return ResponseEntity.ok(delivery);
    }

    @PostMapping("/{deliveryId}/delivered")
    public ResponseEntity<DeliveryDto> markAsDelivered(@PathVariable UUID deliveryId) {
        DeliveryDto delivery = deliveryService.markAsDelivered(deliveryId);
        return ResponseEntity.ok(delivery);
    }

    @PostMapping("/{deliveryId}/failed")
    public ResponseEntity<DeliveryDto> markAsFailed(@PathVariable UUID deliveryId) {
        DeliveryDto delivery = deliveryService.markAsFailed(deliveryId);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> getDelivery(@PathVariable UUID deliveryId) {
        DeliveryDto delivery = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(delivery);
    }
}