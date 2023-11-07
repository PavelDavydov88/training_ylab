package org.davydov.service;

import io.jsonwebtoken.Claims;
import org.davydov.model.AuthDTO;
import org.davydov.model.PlayerDTO;

import java.sql.SQLException;
import java.util.Optional;

public interface AuthService {
    Optional<String> doAuthorization(AuthDTO dto) throws SQLException;

    void exitAuthorization(String token) throws SQLException;

    Claims decodeJWT(String jwt);

    String createJWT(String id, String issuer, String subject, long ttlMillis);

    Optional<String> find(String token) throws SQLException;
}
