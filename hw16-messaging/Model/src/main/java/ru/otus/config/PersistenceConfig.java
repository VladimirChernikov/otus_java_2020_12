package ru.otus.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(basePackageClasses = {ru.otus.repository.PersistenceConfigRoot.class})
public class PersistenceConfig {

    @Bean(name = "demoDbDataSource")
    @ConfigurationProperties("app.datasource.demo-db")
    public DataSource demoDbDataSource() {
        return DataSourceBuilder.create().build();
    }

}
