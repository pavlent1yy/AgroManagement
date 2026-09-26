package com.pavlent1yy.agro_management.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FieldPlanForm {

    @NotBlank(message = "Культура обязательна")
    @Size(max = 100, message = "Название культуры не должно превышать 100 символов")
    private String culture;

    @NotBlank(message = "Поле обязательно")
    @Size(max = 100, message = "Название поля не должно превышать 100 символов")
    private String fieldName;

    @DecimalMin(value = "0.01", message = "Площадь должна быть больше нуля")
    @Digits(integer = 10, fraction = 2, message = "Площадь должна содержать не более 10 целых и 2 десятичных знаков")
    private BigDecimal area;

    @NotBlank(message = "Сезон обязателен")
    @Size(max = 20, message = "Сезон не должен превышать 20 символов")
    private String season;
}
