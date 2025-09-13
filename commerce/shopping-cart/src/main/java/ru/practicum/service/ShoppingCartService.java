package ru.practicum.service;

import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.store.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto addProductsInCart(String username, Map<UUID, Long> products);

    ShoppingCartDto getActiveShoppingCartOfUser(String username);

    void deactivateCart(String username);

    ShoppingCartDto removeProductsFromCart(String username, List<UUID> productIds);

    ProductDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
