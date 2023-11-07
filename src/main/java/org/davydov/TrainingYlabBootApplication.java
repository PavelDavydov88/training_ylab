package org.davydov;

import org.example.loggingstarter.EnableLoggable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLoggable
@SpringBootApplication
public class TrainingYlabBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrainingYlabBootApplication.class, args);
    }

}
