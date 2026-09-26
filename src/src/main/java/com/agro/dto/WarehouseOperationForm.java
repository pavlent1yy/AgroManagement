package com.agro.dto;

import com.agro.entity.OperationType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WarehouseOperationForm {

    @NotNull(message = "Необходимо выбрать ресурс")
    private Long itemId;

    @NotNull(message = "Тип операции обязателен")
    private OperationType operationType;

    @NotNull(message = "Количество обязательно")
    @DecimalMin(value = "0.01", message = "Количество должно быть больше нуля")
    @Digits(integer = 10, fraction = 2, message = "Количество должно содержать не более 10 целых и 2 десятичных знаков")
    private BigDecimal quantity;
}
