package com.agro.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("Администратор"),
    AGRONOMIST("Агроном"),
    MECHANIC("Механик"),
    STOREKEEPER("Кладовщик");

    private final String displayName;
}
