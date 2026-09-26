package com.agro.repository;

import com.agro.entity.WarehouseItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT item FROM WarehouseItem item WHERE item.id = :id")
    Optional<WarehouseItem> findWithLockById(@Param("id") Long id);
}
