package com.agro.service;

import com.agro.entity.AuditLog;
import com.agro.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String eventType, String description, String username) {
        AuditLog log = AuditLog.builder()
                .eventType(eventType)
                .description(description)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
