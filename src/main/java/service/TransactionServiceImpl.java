package service;

import lombok.RequiredArgsConstructor;
import repository.TransactionRepository;

import java.sql.SQLException;

/**
 * класс предоставляет сервис по работе с транзакциями
 */
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * метод для сохранения транзакции в репозитории
     *
     * @param idPlayer    ID игрока
     * @param transaction номер транзакции
     * @throws SQLException
     */
    @Override
    public void save(Long idPlayer, Long transaction) throws SQLException {
        if (!checkExist(transaction)) {
            transactionRepository.save(idPlayer, transaction);
        } else throw new SQLException("this transaction exist");
    }

    /**
     * метод рповерки существоания номера транзакции
     *
     * @param transaction номера транзакции
     * @return true если номера транзакции существует, false если нет
     */
    @Override
    public boolean checkExist(Long transaction) throws SQLException {

        return transactionRepository.find(transaction) != null;
    }
}
