package service;

public interface AuthService {
    String doAuthorization(String name, String password);

    void exitAuthorization(String token);

}
