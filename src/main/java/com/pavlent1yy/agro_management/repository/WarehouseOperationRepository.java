package com.pavlent1yy.agro_management.repository;

import com.pavlent1yy.agro_management.entity.WarehouseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseOperationRepository extends JpaRepository<WarehouseOperation, Long> {
    @Query("SELECT operation FROM WarehouseOperation operation JOIN FETCH operation.item ORDER BY operation.operationDate DESC, operation.id DESC")
    List<WarehouseOperation> findAllWithItemsOrderByOperationDateDesc();
}
