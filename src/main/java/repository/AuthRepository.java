package repository;

public interface AuthRepository {
    void save(String token);

    String find(String token);

    void delete(String token);
}
