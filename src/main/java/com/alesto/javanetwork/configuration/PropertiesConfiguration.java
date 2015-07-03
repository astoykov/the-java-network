package com.alesto.javanetwork.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(
        value = {"classpath:/properties/application.properties"}
)
public class PropertiesConfiguration {

    /**
     * This method enables support of {@link org.springframework.beans.factory.annotation.Value @Value} annotation.
     * Use {@link org.springframework.context.annotation.PropertySource @PropertySource} annotation to add properties.
     *
     * @return blank PropertySourcesPlaceholderConfigurer instance
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertySourcesPlaceholderConfigurer;
    }

}
