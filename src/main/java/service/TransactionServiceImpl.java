package service;

import repository.TransactionRepository;

/**
 * класс предоставляет сервис по работе с транзакциями
 */
public class TransactionServiceImpl implements TransactionService {

    private  final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * метод для сохранения транзакции в репозитории
     * @param transaction номер транзакции
     * @return возращает номер транзакции
     */
    @Override
    public String save(String transaction) {
        if (!checkExist(transaction)) {
            return transactionRepository.save(transaction);
        }
        else return null;
    }

    /**
     * метод рповерки существоания номера транзакции
     * @param transaction номера транзакции
     * @return true если номера транзакции существует, false если нет
     */
    @Override
    public boolean checkExist(String transaction) {
        return transactionRepository.find(transaction) != null;
    }
}
