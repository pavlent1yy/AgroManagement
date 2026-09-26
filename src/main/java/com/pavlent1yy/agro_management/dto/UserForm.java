package com.pavlent1yy.agro_management.dto;

import com.pavlent1yy.agro_management.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    private Long id;

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String username;

    @NotBlank(message = "ФИО обязательно")
    @Size(max = 150, message = "ФИО не должно превышать 150 символов")
    private String fullName;

    @NotNull(message = "Роль обязательна")
    private Role role;

    @Size(max = 100, message = "Пароль не должен превышать 100 символов")
    private String password;

    private Boolean active = true;
}
