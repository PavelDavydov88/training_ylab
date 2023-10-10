package repository;

public interface TransactionRepository {

    String save(String transaction);

    String find(String transaction);

}
