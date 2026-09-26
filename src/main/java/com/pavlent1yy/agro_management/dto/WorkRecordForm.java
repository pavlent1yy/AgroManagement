package com.pavlent1yy.agro_management.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class WorkRecordForm {

    @NotNull(message = "Необходимо выбрать технику")
    private Long equipmentId;

    @NotNull(message = "Моточасы обязательны")
    @DecimalMin(value = "0.01", message = "Моточасы должны быть больше нуля")
    @Digits(integer = 6, fraction = 2, message = "Моточасы должны содержать не более 6 целых и 2 десятичных знаков")
    private BigDecimal hours;

    @NotNull(message = "Расход топлива обязателен")
    @DecimalMin(value = "0.00", message = "Расход топлива не может быть отрицательным")
    @Digits(integer = 8, fraction = 2, message = "Расход топлива должен содержать не более 8 целых и 2 десятичных знаков")
    private BigDecimal fuelConsumed;

    @NotNull(message = "Дата обязательна")
    @PastOrPresent(message = "Дата не может быть в будущем")
    private LocalDate recordDate;
}
