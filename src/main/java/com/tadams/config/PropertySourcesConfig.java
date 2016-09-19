package com.tadams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static com.tadams.config.PropertySourcesConfig.Environment.*;

@Configuration
@SuppressWarnings("UnusedDeclaration")
public class PropertySourcesConfig {

    enum Environment {
        DEV,
        TEST,
        QA,
        PROD;

        public Resource[] getProperties() {
            String propertyFileName = this.name().toLowerCase() + ".properties";
            return new Resource[] { new ClassPathResource(propertyFileName) };
        }
    }

    @Profile("dev")
    @SuppressWarnings("UnusedDeclaration")
    public static class DevConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return getPlaceholdConfigurer(DEV);
        }
    }

    @Profile("test")
    @SuppressWarnings("UnusedDeclaration")
    public static class TestConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return getPlaceholdConfigurer(TEST);
        }
    }

    @Profile("qa")
    @SuppressWarnings("UnusedDeclaration")
    public static class QAConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return getPlaceholdConfigurer(QA);
        }
    }

    @Profile("prod")
    @SuppressWarnings("UnusedDeclaration")
    public static class ProdConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return getPlaceholdConfigurer(PROD);
        }
    }

    public static PropertySourcesPlaceholderConfigurer getPlaceholdConfigurer(PropertySourcesConfig.Environment environment) {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocations(environment.getProperties());
        return pspc;
    }
}