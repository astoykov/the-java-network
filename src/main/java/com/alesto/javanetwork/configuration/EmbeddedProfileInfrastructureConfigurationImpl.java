package com.alesto.javanetwork.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"test", "embedded"})
@EnableJpaRepositories("com.alesto.javanetwork.repository")
public class EmbeddedProfileInfrastructureConfigurationImpl implements InfrastructureConfiguration {

    @Bean(destroyMethod = "shutdown")
    @Override
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();

        builder
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScripts(
                        "db/embedded_compatibility_mode.sql"
                );

        return builder.build();
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
        // disable hibernate.hbm2ddl.auto - Flyway is used to create schemas
        //jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.H2);

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