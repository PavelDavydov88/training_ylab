package service;

import org.junit.jupiter.api.Test;
import repository.AuditRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuditServiceImplTest {

    private final AuditRepository auditRepository = mock(AuditRepository.class);

    AuditService auditService = new AuditServiceImpl(auditRepository);

    @Test
    public void testThatSendEvent() throws SQLException {
        auditService.sendEvent(1, "history");
        verify(auditRepository).save(anyLong(), anyString());
    }

    @Test
    public void testThatGetEvents() throws Exception {
        final List<String> dataList = new ArrayList<>();
        dataList.add("event 1");
        dataList.add("event 2");
        when(auditRepository.findAllById(1)).thenReturn(dataList);
        List<String> listEvent = auditService.getEvents(1);
        assertEquals(2, listEvent.size());
    }
}