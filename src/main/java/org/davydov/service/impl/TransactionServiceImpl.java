package org.davydov.service.impl;

import lombok.RequiredArgsConstructor;
import org.davydov.repository.TransactionRepository;
import org.davydov.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Класс предоставляет сервис по работе с транзакциями
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Метод для сохранения транзакции в репозитории
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
     * Метод проверки существоания номера транзакции
     *
     * @param transaction номер транзакции
     * @return true если номер транзакции существует, false если нет
     */
    @Override
    public boolean checkExist(Long transaction) throws SQLException {

        return transactionRepository.find(transaction) != null;
    }
}
