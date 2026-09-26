package com.pavlent1yy.agro_management.repository;

import com.pavlent1yy.agro_management.entity.FieldPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldPlanRepository extends JpaRepository<FieldPlan, Long> {

    List<FieldPlan> findAllByOrderBySeasonDescFieldNameAsc();
}
