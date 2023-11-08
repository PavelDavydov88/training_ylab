package org.example.auditstarter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Класс конфигурации аудита
 */
@ComponentScan(basePackages = "org.example.auditstarter")
@AutoConfiguration
public class AuditAutoConfiguration {
}