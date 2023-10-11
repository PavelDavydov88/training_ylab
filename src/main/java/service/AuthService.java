package service;

import io.jsonwebtoken.Claims;

public interface AuthService {
    String doAuthorization(String name, String password);

    void exitAuthorization(String token);

    Claims decodeJWT(String jwt);

    String createJWT(String id, String issuer, String subject, long ttlMillis);

    String find(String token);
}
