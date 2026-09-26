package com.agro.service;

import com.agro.dto.WarehouseOperationForm;
import com.agro.entity.OperationType;
import com.agro.entity.WarehouseItem;
import com.agro.entity.WarehouseOperation;
import com.agro.repository.WarehouseItemRepository;
import com.agro.repository.WarehouseOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseItemRepository itemRepository;
    private final WarehouseOperationRepository operationRepository;
    private final AuditService auditService;

    @Transactional
    public WarehouseOperation saveOperation(WarehouseOperationForm form) {
        WarehouseItem item = itemRepository.findWithLockById(form.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Ресурс не найден"));

        if (form.getOperationType() == OperationType.OUTCOME
                && item.getQuantity().compareTo(form.getQuantity()) < 0) {
            throw new IllegalArgumentException("Недостаточно ресурса на складе: доступно "
                    + item.getQuantity() + " " + item.getUnit());
        }

        if (form.getOperationType() == OperationType.INCOME) {
            item.setQuantity(item.getQuantity().add(form.getQuantity()));
        } else {
            item.setQuantity(item.getQuantity().subtract(form.getQuantity()));
        }

        WarehouseOperation operation = WarehouseOperation.builder()
                .item(item)
                .operationType(form.getOperationType())
                .quantity(form.getQuantity())
                .operationDate(LocalDate.now())
                .build();

        WarehouseOperation saved = operationRepository.save(operation);
        itemRepository.save(item);

        String username = currentUsername();
        auditService.log("WAREHOUSE_OPERATION",
                form.getOperationType().getDisplayName() + ": " + item.getName() + ", "
                        + form.getQuantity() + " " + item.getUnit(), username);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<WarehouseItem> findAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WarehouseOperation> findAllOperations() {
        return operationRepository.findAllWithItemsOrderByOperationDateDesc();
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }
}
