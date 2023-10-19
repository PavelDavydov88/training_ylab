package service;

import model.Player;
import org.junit.Test;
import repository.TransactionRepository;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


public class TransactionServiceImplTest {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    TransactionService transactionService = new TransactionServiceImpl(transactionRepository);

    @Test
    public void testThatSaveTransaction() throws SQLException {
        transactionService.save(1L, "1");
        verify(transactionRepository).save(anyLong(), anyString());
    }

    @Test
    public void testThatTransactionExist() throws SQLException {
        when(transactionRepository.find(anyString())).thenReturn("1");
        assertThat(transactionService.checkExist("1")).isTrue();

    }
}