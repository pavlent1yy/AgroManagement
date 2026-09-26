package com.pavlent1yy.agro_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "field_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String culture;

    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal area;

    @Column(nullable = false, length = 20)
    private String season;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;
}
