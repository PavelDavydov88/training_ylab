package org.davydov.service;

import org.example.auditstarter.repository.AuditRepository;
import org.example.auditstarter.service.AuditService;
import org.example.auditstarter.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class AuditServiceImplTest {

    private final AuditRepository auditRepository = mock(AuditRepository.class);

    AuditService auditService = new AuditServiceImpl(auditRepository);

    @Test
    public void testThatSendEvent() throws SQLException {
        auditService.sendEvent(1, "history");
        verify(auditRepository).save(anyLong(), anyString());
    }
}