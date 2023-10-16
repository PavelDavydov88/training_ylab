package service;

import io.jsonwebtoken.Claims;

import java.sql.SQLException;

public interface AuthService {
    String doAuthorization(String name, String password) throws SQLException;

    void exitAuthorization(String token) throws SQLException;

    Claims decodeJWT(String jwt);

    String createJWT(String id, String issuer, String subject, long ttlMillis);

    String find(String token);
}
