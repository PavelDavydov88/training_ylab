package service;

import io.jsonwebtoken.Claims;
import model.PlayerDTO;

import java.sql.SQLException;
import java.util.Optional;

public interface AuthService {
    Optional<String> doAuthorization(PlayerDTO dto) throws SQLException;

    void exitAuthorization(String token) throws SQLException;

    Claims decodeJWT(String jwt);

    String createJWT(String id, String issuer, String subject, long ttlMillis);

    Optional<String> find(String token) throws SQLException;
}
