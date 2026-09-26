package com.agro.repository;

import com.agro.entity.FieldPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldPlanRepository extends JpaRepository<FieldPlan, Long> {

    List<FieldPlan> findAllByOrderBySeasonDescFieldNameAsc();
}
