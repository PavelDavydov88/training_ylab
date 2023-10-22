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
        transactionService.save(1L, 1L);
        verify(transactionRepository).save(anyLong(), anyLong());
    }

    @Test
    public void testThatTransactionExist() throws SQLException {
        when(transactionRepository.find(anyLong())).thenReturn(1L);
        assertThat(transactionService.checkExist(1L)).isTrue();

    }
}