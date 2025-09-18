package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.enums.order.OrderState;
import ru.practicum.exceptions.NoOrderFoundException;
import ru.practicum.mapper.OrderMapper;
import ru.practicum.model.Order;
import ru.practicum.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest createOrderRequest, String username) {
        log.info("Creating new order for user: {}", username);
        
        Order order = Order.builder()
                .username(username)
                .shoppingCartId(createOrderRequest.getShoppingCartId())
                .state(OrderState.NEW)
                .products(createOrderRequest.getProducts())
                .totalVolume(createOrderRequest.getTotalVolume())
                .totalWeight(createOrderRequest.getTotalWeight())
                .isFragile(createOrderRequest.getIsFragile())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public Page<OrderDto> getOrdersOfUser(String username, Integer page, Integer size) {
        log.info("Getting orders for user: {}, page: {}, size: {}", username, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByUsername(username, pageable);
        
        return OrderMapper.toOrderDtoPage(orders);
    }

    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {
        log.info("Processing return for order: {}", returnRequest.getOrderId());
        
        Order order = orderRepository.findById(returnRequest.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + returnRequest.getOrderId()));
        
        order.setState(OrderState.PRODUCT_RETURNED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order returned with ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto payOrder(UUID orderId) {
        log.info("Processing payment for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.ON_PAYMENT);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order payment initiated for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto changeStateToPaymentFailed(UUID orderId) {
        log.info("Changing order state to payment failed for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.PAYMENT_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order payment failed for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto sendOrderToDelivery(UUID orderId) {
        log.info("Sending order to delivery for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.ON_DELIVERY);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order sent to delivery for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto changeStateToDeliveryFailed(UUID orderId) {
        log.info("Changing order state to delivery failed for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.DELIVERY_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order delivery failed for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto changeStateToCompleted(UUID orderId) {
        log.info("Changing order state to completed for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order completed for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto calculateOrderTotalPrice(UUID orderId) {
        log.info("Calculating total price for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        // TODO: Implement price calculation logic
        order.setTotalPrice(0.0);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Total price calculated for order ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto calculateOrderDeliveryPrice(UUID orderId) {
        log.info("Calculating delivery price for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        // TODO: Implement delivery price calculation logic
        order.setDeliveryPrice(0.0);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Delivery price calculated for order ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto sendOrderToAssembly(UUID orderId) {
        log.info("Sending order to assembly for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.ASSEMBLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order sent to assembly for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto changeOrderStateToAssemblyFailed(UUID orderId) {
        log.info("Changing order state to assembly failed for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order not found with ID: " + orderId));
        
        order.setState(OrderState.ASSEMBLY_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order assembly failed for ID: {}", savedOrder.getId());
        
        return OrderMapper.toOrderDto(savedOrder);
    }
}