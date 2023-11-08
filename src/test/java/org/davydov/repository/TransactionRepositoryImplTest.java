//package org.davydov.repository;
//
//import liquibase.exception.LiquibaseException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class TransactionRepositoryImplTest extends RepositoryLiquibaseInit {
//
//    @Autowired
//    TransactionRepository transactionRepository;
//    public static final String INSERT_TRANSACTION = """
//            INSERT INTO wallet."transaction" ("id" , "id_player","transaction")
//            VALUES (nextval( 'wallet.sequence_transaction'), 10,'1')
//            """;
//
//    @BeforeEach
//    public void setUp() throws SQLException, LiquibaseException {
//        super.setUp();
//        statement.executeUpdate(INSERT_TRANSACTION);
//    }
//
//    @Test
//    public void testThatSaveTransaction() throws SQLException {
//        transactionRepository.save(1l, 2L);
//        Long transaction = transactionRepository.find(2L);
//        assertEquals(2L, transaction);
//    }
//
//    @Test
//    public void thatFindByNameTransaction() throws SQLException {
//        Long transaction = transactionRepository.find(1L);
//        assertEquals(1L, transaction);
//    }
//
//    @Test
//    public void thatFindByNameTransactionReturnNull() throws SQLException {
//        Long transaction = transactionRepository.find(0L);
//        assertNull(transaction);
//    }
//}