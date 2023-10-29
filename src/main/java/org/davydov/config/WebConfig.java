package org.davydov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

//    @Value("${db.changeLog}")
//    private String changeLog;
//    @Value("${db.url}")
//    private String url;
//    @Value("${db.user}")
//    private String username;
//    @Value("${db.password}")
//    private String password;

//    @Bean
//    public UtilLiquibase utilLiquibase() {
//        UtilLiquibase utilLiquibase = new UtilLiquibase();
//        utilLiquibase.initLiquibase();
//        return utilLiquibase;
//    }


}

