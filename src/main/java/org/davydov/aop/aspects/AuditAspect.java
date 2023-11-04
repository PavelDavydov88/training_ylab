package org.davydov.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.davydov.aop.annotations.Audit;
import org.davydov.model.PlayerDTO;
import org.davydov.repository.PlayerRepository;
import org.davydov.service.AuditService;
import org.davydov.service.AuthService;
import org.springframework.stereotype.Component;

/**
 * Аспект для аудита
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuthService authService;
    private final PlayerRepository playerRepository;
    private final AuditService auditService;

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
