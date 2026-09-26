package com.agro.repository;

import com.agro.entity.WarehouseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseOperationRepository extends JpaRepository<WarehouseOperation, Long> {

    // JOIN FETCH предотвращает LazyInitializationException при выводе связанного ресурса в Thymeleaf.
    @Query("SELECT operation FROM WarehouseOperation operation JOIN FETCH operation.item ORDER BY operation.operationDate DESC, operation.id DESC")
    List<WarehouseOperation> findAllWithItemsOrderByOperationDateDesc();
}
