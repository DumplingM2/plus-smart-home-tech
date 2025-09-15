package ru.practicum.dto.store;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pageable {
    @Min(value = 0)
    Integer page = 0;
    @Min(value = 1)
    Integer size = 1;
    List<String> sort = Collections.emptyList();
    String direction;
}
