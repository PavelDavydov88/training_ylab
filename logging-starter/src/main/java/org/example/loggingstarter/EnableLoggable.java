package org.example.loggingstarter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LoggingConfig.class)
@Inherited
public @interface EnableLoggable {
}