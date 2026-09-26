package com.pavlent1yy.agro_management.repository;

import com.pavlent1yy.agro_management.entity.WorkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {
    @Query("SELECT record FROM WorkRecord record JOIN FETCH record.equipment ORDER BY record.recordDate DESC, record.id DESC")
    List<WorkRecord> findAllWithEquipmentOrderByRecordDateDesc();
}
