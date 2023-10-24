package aop.aspects;

import config.DBConnectionProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import repository.AuditRepository;
import repository.AuditRepositoryImpl;
import service.AuditService;
import service.AuditServiceImpl;

import java.io.IOException;

import static config.PropertyUtils.getProperty;
import static config.PropertyUtils.getProperty;

@Aspect
public class AuditAspect {

    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    AuditService auditService = new AuditServiceImpl(auditRepository);

    public AuditAspect() throws IOException {
    }

    @Pointcut("within(@aop.annotations.Audit *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        auditService.sendEvent();
    return null;
    }

}
