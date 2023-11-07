package org.davydov.service;

import org.davydov.repository.AuditRepository;
import org.davydov.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class AuditServiceImplTest {

    private final AuditRepository auditRepository = mock(AuditRepository.class);
    private final AuthService authService = mock(AuthService.class);

    AuditService auditService = new AuditServiceImpl(auditRepository, authService);

    @Test
    public void testThatSendEvent() throws SQLException {
        auditService.sendEvent(1, "history");
        verify(auditRepository).save(anyLong(), anyString());
    }
}