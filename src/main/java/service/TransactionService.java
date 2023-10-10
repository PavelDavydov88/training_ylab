package service;

public interface TransactionService {

    String save(String transaction);

    boolean checkExist(String transaction);
}
