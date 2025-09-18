package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody CreateNewOrderRequest createOrderRequest,
                                                   @RequestHeader("X-User-Name") String username) {
        OrderDto order = orderService.createNewOrder(createOrderRequest, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrdersOfUser(@RequestHeader("X-User-Name") String username,
                                                          @RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderDto> orders = orderService.getOrdersOfUser(username, page, size);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/return")
    public ResponseEntity<OrderDto> returnOrder(@PathVariable UUID orderId,
                                               @RequestBody ProductReturnRequest returnRequest) {
        OrderDto order = orderService.returnOrder(returnRequest);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderDto> payOrder(@PathVariable UUID orderId) {
        OrderDto order = orderService.payOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/payment-failed")
    public ResponseEntity<OrderDto> changeStateToPaymentFailed(@PathVariable UUID orderId) {
        OrderDto order = orderService.changeStateToPaymentFailed(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/delivery")
    public ResponseEntity<OrderDto> sendOrderToDelivery(@PathVariable UUID orderId) {
        OrderDto order = orderService.sendOrderToDelivery(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/delivery-failed")
    public ResponseEntity<OrderDto> changeStateToDeliveryFailed(@PathVariable UUID orderId) {
        OrderDto order = orderService.changeStateToDeliveryFailed(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/completed")
    public ResponseEntity<OrderDto> changeStateToCompleted(@PathVariable UUID orderId) {
        OrderDto order = orderService.changeStateToCompleted(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/total-price")
    public ResponseEntity<OrderDto> calculateOrderTotalPrice(@PathVariable UUID orderId) {
        OrderDto order = orderService.calculateOrderTotalPrice(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/delivery-price")
    public ResponseEntity<OrderDto> calculateOrderDeliveryPrice(@PathVariable UUID orderId) {
        OrderDto order = orderService.calculateOrderDeliveryPrice(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/assembly")
    public ResponseEntity<OrderDto> sendOrderToAssembly(@PathVariable UUID orderId) {
        OrderDto order = orderService.sendOrderToAssembly(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/assembly-failed")
    public ResponseEntity<OrderDto> changeOrderStateToAssemblyFailed(@PathVariable UUID orderId) {
        OrderDto order = orderService.changeOrderStateToAssemblyFailed(orderId);
        return ResponseEntity.ok(order);
    }
}