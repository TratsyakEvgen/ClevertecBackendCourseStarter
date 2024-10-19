package ru.clevertec.starter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.starter.bpp.SessionBeanPostProcessor;
import ru.clevertec.starter.service.BlackListChecker;
import ru.clevertec.starter.service.SessionSupplier;
import ru.clevertec.starter.service.impl.HttpSessionSupplier;
import ru.clevertec.starter.service.impl.PropertyBlackListChecker;
import ru.clevertec.starter.service.impl.SessionInterceptor;

import java.util.List;

@AutoConfiguration
@ConfigurationProperties(prefix = "black-list")
@Getter
@Setter
public class Config {
    private List<String> logins;

    @Bean
    @ConditionalOnMissingBean
    public SessionBeanPostProcessor sessionBeanPostProcessor() {
        return new SessionBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "black-list", name = "enabled", havingValue = "true")
    public BlackListChecker blackListChecker() {
        return new PropertyBlackListChecker(logins);
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionSupplier sessionSupplier(RestTemplate restTemplate) {
        return new HttpSessionSupplier(restTemplate, "http://localhost:8080/sessions");
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
