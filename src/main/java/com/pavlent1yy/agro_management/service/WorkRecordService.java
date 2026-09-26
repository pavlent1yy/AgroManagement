package com.pavlent1yy.agro_management.service;

import com.pavlent1yy.agro_management.dto.WorkRecordForm;
import com.pavlent1yy.agro_management.entity.Equipment;
import com.pavlent1yy.agro_management.entity.WorkRecord;
import com.pavlent1yy.agro_management.repository.EquipmentRepository;
import com.pavlent1yy.agro_management.repository.WorkRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkRecordService {

    private final WorkRecordRepository workRecordRepository;
    private final EquipmentRepository equipmentRepository;
    private final AuditService auditService;

    @Transactional
    public WorkRecord save(WorkRecordForm form) {
        Equipment equipment = equipmentRepository.findById(form.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Техника не найдена"));

        WorkRecord record = WorkRecord.builder()
                .equipment(equipment)
                .hours(form.getHours())
                .fuelConsumed(form.getFuelConsumed())
                .recordDate(form.getRecordDate())
                .build();
        WorkRecord saved = workRecordRepository.save(record);

        String username = currentUsername();
        auditService.log("CREATE_WORK_RECORD",
                "Учтена работа техники " + equipment.getName() + " за " + form.getRecordDate(), username);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<WorkRecord> findAll() {
        return workRecordRepository.findAllWithEquipmentOrderByRecordDateDesc();
    }

    @Transactional(readOnly = true)
    public List<Equipment> findAllEquipment() {
        return equipmentRepository.findAll();
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }
}
