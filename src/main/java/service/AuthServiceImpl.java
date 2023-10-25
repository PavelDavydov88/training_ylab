package service;

import aop.annotations.Audit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import model.Player;
import model.PlayerDTO;
import repository.AuthRepository;
import repository.PlayerRepository;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * класс предоставляет сервис по авторизации и завершения работы игрока
 */
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
//    private final AuditRepository auditRepository;

    /**
     * метод авторизации игрока
     *
     * @param dto@return токен
     */
    @Audit(success = "authorization completed successful")
    @Override
    public Optional<String> doAuthorization(PlayerDTO dto) throws SQLException {

        Player player = null;
        try {
            player = playerRepository.findByNamePassword(dto);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        String token = createJWT(String.valueOf(player.getId()), "test", player.toString(), System.currentTimeMillis());
        authRepository.save(token);
//        auditRepository.save(player.getId(), "authorization completed successful");
        return Optional.of(token);
    }

    /**
     * метод по удалению токена из репозитория
     *
     * @param token токен игрока
     */
    @Audit(success = "exit of authorization player done")
    @Override
    public void exitAuthorization(String token) throws SQLException {
        int idPlayer = Integer.parseInt(decodeJWT(token).getId());
        Player player = null;
        try {
            player = playerRepository.findById(idPlayer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        auditRepository.save(player.getId(), "exit of authorization player done");
        authRepository.delete(token);
    }

    /**
     * метод генерации токена
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
     * метод декодинга токена
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
