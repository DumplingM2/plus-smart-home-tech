package ru.practicum.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  ChangeProductQuantityRequest {
    @NotNull
    UUID productId;
    @NotNull
    @Min(value = 1)
    Long newQuantity;
}
