package org.davydov.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.davydov.aop.annotations.Audit;
import org.davydov.model.AuthDTO;
import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.davydov.repository.AuthRepository;
import org.davydov.repository.PlayerRepository;
import org.davydov.service.AuthService;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * Класс предоставляет сервис по авторизации и завершения работы игрока
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;

    /**
     * Метод авторизации игрока
     *
     * @param dto DTO игрока
     * @return возращает токен опционально
     * @throws SQLException
     */
    @Audit(success = "authorization completed successful")
    @Override
    public Optional<String> doAuthorization(AuthDTO dto) throws SQLException {
        Player player = null;
        PlayerDTO playerDTO = new PlayerDTO(dto.getName(), dto.getPassword());
        try {
            player = playerRepository.findByNamePassword(playerDTO);
            if (player.getId() != dto.getIdPlayer()) {
                throw new SQLException("the ID is not equal to");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        String token = createJWT(String.valueOf(player.getId()), "test", player.toString(), System.currentTimeMillis());
        authRepository.save(token);
        return Optional.of(token);
    }

    /**
     * Метод для удаления токена из репозитория
     *
     * @param token токен игрока
     */
    @Audit(success = "exit of authorization player done")
    @Override
    public void exitAuthorization(String token) throws SQLException {
        int idPlayer = Integer.parseInt(decodeJWT(token).getId());
        try {
            playerRepository.findById(idPlayer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        authRepository.delete(token);
    }

    /**
     * Метод генерации токена
     *
     * @param id        id токена
     * @param issuer    эмитент токена
     * @param subject   тело токена
     * @param ttlMillis время
     * @return новый токен
     */
    @Override
    public String createJWT(String id, String issuer, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("SECRET_KEY");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * Метод декодинга токена
     *
     * @param jwt токен
     * @return расшифрованный токен
     */
    @Override
    public Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("SECRET_KEY"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * Метод возвращает токен из репозитория если такой токен существует
     *
     * @param token токен
     * @return токен
     */
    @Override
    public Optional<String> find(String token) throws SQLException {
        return authRepository.find(token);
    }

}
