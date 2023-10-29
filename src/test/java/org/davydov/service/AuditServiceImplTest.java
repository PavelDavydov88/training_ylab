package org.davydov.service;

import org.davydov.model.Player;
import org.davydov.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.Test;
import org.davydov.repository.AuditRepository;

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

    //    @Test
//    public void testThatGetEvents() throws Exception {
//        final List<String> dataList = new ArrayList<>();
////        dataList.add("event 1");
////        dataList.add("event 2");
////        when(auditRepository.findAllById(1)).thenReturn(dataList);
//        when(authService.find(anyString())).thenReturn( Optional.of("any token"));
//        when(authService.decodeJWT(anyString())).thenReturn(Claims);
////        List<String> listEvent = auditService.getListAuditAction(anyString());
////        assertEquals(2, listEvent.size());
////        when(authService.createJWT("1", createDefaultPlayer(), System.currentTimeMillis()))
//        auditService.getListAuditAction("any token");
//        verify(authService).find(anyString());
//        verify(auditRepository).findAllById(anyInt());
//
//    }
    Player createDefaultPlayer() {
        return new Player(1, "Pavel", "password", 0);
    }
}