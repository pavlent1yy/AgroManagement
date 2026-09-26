package com.agro.repository;

import com.agro.entity.WorkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {

    // JOIN FETCH предотвращает LazyInitializationException при выводе связанной техники в Thymeleaf.
    @Query("SELECT record FROM WorkRecord record JOIN FETCH record.equipment ORDER BY record.recordDate DESC, record.id DESC")
    List<WorkRecord> findAllWithEquipmentOrderByRecordDateDesc();
}
