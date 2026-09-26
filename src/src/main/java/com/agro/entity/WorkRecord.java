package com.agro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "work_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal hours;

    @Column(name = "fuel_consumed", nullable = false, precision = 10, scale = 2)
    private BigDecimal fuelConsumed;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
}
