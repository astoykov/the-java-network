package com.alesto.javanetwork.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"mysql"})
@EnableJpaRepositories("com.alesto.javanetwork.repository")
public class MySqlProfileInfrastructureConfigurationImpl implements InfrastructureConfiguration {

    @Inject
    private Environment environment;

    @Bean(destroyMethod = "close")
    @Override
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + environment.getProperty("dbMysqlHost") + ":" + environment.getProperty("dbMysqlPort") + "/" + environment.getProperty("dbMysqlDatabase"));
        dataSource.setUsername(environment.getProperty("dbMysqlUsername"));
        dataSource.setPassword(environment.getProperty("dbMysqlPassword"));
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setValidationQuery("SELECT 1");

        return dataSource;
    }

    @Bean
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        Map<String, String> jpaProperties = new HashMap<String, String>();
        jpaProperties.put("javax.persistence.validation.mode", "NONE");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);

        entityManagerFactory.setPackagesToScan("com.alesto.javanetwork.domain.entity");
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaPropertyMap(jpaProperties);

        return entityManagerFactory;
    }

    @Bean
    @Override
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public Flyway flyway() {
        Flyway flyway = new Flyway();

        flyway.setDataSource(dataSource());
        flyway.setTable("schema_version");
        flyway.setLocations("db/migration");
        flyway.setEncoding("UTF-8");
        flyway.setValidateOnMigrate(true);

        flyway.migrate();

        return flyway;
    }

}