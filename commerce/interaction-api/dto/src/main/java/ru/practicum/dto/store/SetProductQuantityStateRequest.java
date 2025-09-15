package ru.practicum.dto.store;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.enums.QuantityState;

import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SetProductQuantityStateRequest {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
