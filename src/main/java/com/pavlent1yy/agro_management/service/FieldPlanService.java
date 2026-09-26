package com.pavlent1yy.agro_management.service;

import com.pavlent1yy.agro_management.dto.FieldPlanForm;
import com.pavlent1yy.agro_management.entity.FieldPlan;
import com.pavlent1yy.agro_management.repository.FieldPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldPlanService {

    private final FieldPlanRepository fieldPlanRepository;
    private final AuditService auditService;

    @Transactional
    public FieldPlan save(FieldPlanForm form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "system";

        FieldPlan plan = FieldPlan.builder()
                .culture(form.getCulture())
                .fieldName(form.getFieldName())
                .area(form.getArea())
                .season(form.getSeason())
                .createdBy(username)
                .build();
        FieldPlan saved = fieldPlanRepository.save(plan);
        auditService.log("CREATE_FIELD_PLAN",
                "Добавлен план: " + plan.getCulture() + ", поле " + plan.getFieldName() + ", сезон " + plan.getSeason(),
                username);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<FieldPlan> findAll() {
        return fieldPlanRepository.findAllByOrderBySeasonDescFieldNameAsc();
    }
}
