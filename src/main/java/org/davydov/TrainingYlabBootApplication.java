package org.davydov;

import org.example.loggingstarter.LoggableAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class TrainingYlabBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrainingYlabBootApplication.class, args);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public LoggableAspect getLoggingAspect(){
//        return new LoggableAspect();
//    }
}
