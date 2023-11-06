package org.example.loggingstarter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования всех Методов
 */
@Slf4j
@Aspect
//@Component
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class LoggableAspect {


    @Around("execution(* org.davydov..*.*(..))")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method {}", proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution of method {} finished. Execution time is {} ms",
                proceedingJoinPoint.getSignature(), (endTime - startTime));
        return result;
    }
}

