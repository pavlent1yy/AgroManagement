package com.agro.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationType {
    INCOME("Приход"),
    OUTCOME("Расход");

    private final String displayName;
}
