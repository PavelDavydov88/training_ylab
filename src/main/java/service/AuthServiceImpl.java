package service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.Player;
import repository.AuditRepository;
import repository.AuthRepository;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * класс предоставляет сервис по авторизации и завершения работы игрока
 */
public class AuthServiceImpl implements AuthService {

    private final PlayerService playerService;
    private final AuthRepository authRepository;
    private final AuditRepository auditRepository;

    public AuthServiceImpl(PlayerService playerService, AuthRepository authRepository, AuditRepository auditRepository) {
        this.playerService = playerService;
        this.authRepository = authRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * метод авторизации игрока
     * @param name имя игрока
     * @param password пароль игрока
     * @return токен
     */
    @Override
    public String doAuthorization(String name, String password) {

        Player player = playerService.findByNamePassword(name, password);

        if (player == null) {
            return "this player doesn't exist";
        } else {
            String token = createJWT(String.valueOf(player.getId()), "test", player.toString(), System.currentTimeMillis());
            authRepository.save(token);
            auditRepository.save(player.getId(), "authorization completed successful");
            return token;
        }
    }

    /**
     * метод по удалению токена из репозитория
     * @param token токен игрока
     */
    @Override
    public void exitAuthorization(String token) {
        Player player = playerService.findByToken(token);
        auditRepository.save(player.getId(), "exit of authorization player done");
        authRepository.delete(token);
    }

    /**
     * метод генерации токена
     * @param id id токена
     * @param issuer эмитент токена
     * @param subject тело токена
     * @param ttlMillis время
     * @return
     */
    private static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("SECRET_KEY");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
