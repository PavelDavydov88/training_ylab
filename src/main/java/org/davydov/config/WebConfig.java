package org.davydov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.List;

@EnableWebMvc
@Configuration
@EnableSwagger2
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Bean
    public Swagger2DocumentationConfiguration getSwagger2(){
        return new Swagger2DocumentationConfiguration();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.davydov.in.controller"))
                .paths(PathSelectors.any())
                .build()
        .apiInfo(getApiInfo());
    }
    private ApiInfo getApiInfo() {
        return new ApiInfo("Swagger2 Api Documentation",
                "How to generate Swagger documentation for your Rest API",
                "1.0", "urn:tos",
                new Contact("Pavel Davydov", "www.davydovpavel.org", "davydovpavel@mail.com"),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}

