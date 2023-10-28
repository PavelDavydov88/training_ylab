package aop.aspects;

import aop.annotations.Audit;
import config.DBConnectionProvider;
import model.PlayerDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import repository.*;
import service.AuditService;
import service.AuditServiceImpl;
import service.AuthService;
import service.AuthServiceImpl;

import java.io.IOException;

import static config.PropertyUtils.getProperty;

/**
 * Аспект для аудита
 */
@Aspect
public class AuditAspect {
    private final DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    private final AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    private final PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private final AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private final AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private final AuditService auditService = new AuditServiceImpl(auditRepository, authService);

    public AuditAspect() throws IOException {
    }

    @Around("@annotation(annotation)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Audit annotation) throws Throwable {
        Object result = null;
        Exception exception = null;
        long playerId = 0L;
        try {
            result = joinPoint.proceed();
            if (joinPoint.getArgs()[0] instanceof String token) {
                playerId = Long.parseLong(authService.decodeJWT(token).getId());
            } else if (joinPoint.getArgs()[0] instanceof PlayerDTO dto) {
                playerId = playerRepository.findByNamePassword(dto).getId();
            }
        } catch (Exception ex) {
            exception = ex;
        }

        if (exception != null) {
            auditService.sendEvent(playerId, exception.getMessage());
            throw exception;
        }

        auditService.sendEvent(playerId, annotation.success());

        return result;
    }
}
