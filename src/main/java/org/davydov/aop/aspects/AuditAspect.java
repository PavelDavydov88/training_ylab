package org.davydov.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.davydov.aop.annotations.Audit;
import org.davydov.model.AccountOperationDTO;
import org.davydov.model.AuthDTO;
import org.davydov.model.PlayerDTO;
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
            System.out.println("result " + result);
            if (joinPoint.getArgs()[0] instanceof AuthDTO authDTO) {
                playerId = authDTO.getIdPlayer();
            } else {
                if (joinPoint.getArgs()[0] instanceof PlayerDTO) {
                    playerId = (long) result;
                } else {
                    if (joinPoint.getArgs()[0] instanceof AccountOperationDTO accountOperationDTO) {
                        playerId = accountOperationDTO.getIdPlayer();
                    } else {
                        if (joinPoint.getArgs()[0] instanceof Long idPlayer) {
                            playerId = idPlayer;
                        }
                    }
                }
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
