package repository;

import java.util.LinkedList;
import java.util.List;

public class AuthRepositoryImpl implements AuthRepository {

    private static final List<String> listToken = new LinkedList<>();

    @Override
    public void save(String token) {
        listToken.add(token);
    }

    @Override
    public String find(String token) {
        if (listToken.contains(token)) return token;
        else return null;
    }

    @Override
    public void delete(String token) {
        listToken.remove(token);
    }
}
