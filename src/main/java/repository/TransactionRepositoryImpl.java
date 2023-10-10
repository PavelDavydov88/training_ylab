package repository;

import java.util.HashSet;
import java.util.Set;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Set<String> setTransaction = new HashSet<>();

    @Override
    public String save(String transaction) {
        setTransaction.add(transaction);
        return transaction;
    }

    @Override
    public String find(String transaction) {
        if (!setTransaction.contains(transaction)) return null;
        else return transaction;
    }
}
