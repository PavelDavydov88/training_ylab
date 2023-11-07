package org.davydov.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.davydov.aop.annotations.Audit;
import org.davydov.service.AuditService;
import org.springframework.stereotype.Component;

/**
 * Аспект для аудита
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(annotation)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Audit annotation) throws Throwable {
        Object result = null;
        Exception exception = null;
        long playerId = 0L;
        try {
            result = joinPoint.proceed();
            if (joinPoint.getArgs()[0] instanceof Long idPlayer) {
                playerId = idPlayer;
            } else {
                playerId = (long) result;
            }
            if (playerId != 0L) {
                auditService.sendEvent(playerId, annotation.success());
            }
        } catch (Exception ex) {
            exception = ex;
        }
        if (exception != null) {
            auditService.sendEvent(playerId, exception.getMessage());
            throw exception;
        }
        return result;
    }

}
