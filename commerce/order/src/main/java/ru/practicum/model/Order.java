package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.order.OrderState;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "shopping_cart_id", nullable = false)
    private UUID shoppingCartId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private OrderState state;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;

    @Column(name = "total_volume")
    private Double totalVolume;

    @Column(name = "total_weight")
    private Double totalWeight;

    @Column(name = "is_fragile")
    private Boolean isFragile;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "products_price")
    private Double productsPrice;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}