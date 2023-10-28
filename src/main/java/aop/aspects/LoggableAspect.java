package aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Аспект для логирования всех Методов
 */
@Aspect
@Slf4j
public class LoggableAspect {

    @Around("execution(* *(..))")
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

