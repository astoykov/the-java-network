package com.alesto.javanetwork.configuration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public interface InfrastructureConfiguration {

    @Bean
    public DataSource dataSource();

    @Bean
    public FactoryBean<EntityManagerFactory> entityManagerFactory();

    @Bean
    public PlatformTransactionManager transactionManager();

}
