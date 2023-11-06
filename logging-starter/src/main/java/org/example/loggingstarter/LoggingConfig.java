package org.example.loggingstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@AutoConfiguration
public class LoggingConfig {


    @Bean

    public LoggableAspect getLoggingAspect() {
        System.out.println("bean LoggableAspect created");
        return new LoggableAspect();
    }
}
