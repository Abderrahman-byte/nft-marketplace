package org.merchantech.nftproject.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("org.merchantech.nftproject")
@PropertySource("classpath:env.properties")
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource () {
        String url = environment.getProperty("jdbc.url");
        String driver = environment.getProperty("jdbc.driver");
        String username = environment.getProperty("jdbc.user");
        String password = environment.getProperty("jdbc.password");

        return DataSourceBuilder.create().username(username).password(password).url(url).driverClassName(driver).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory () {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("org.merchantech.nftproject.models");
        entityManagerFactoryBean.setJpaProperties(addProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager () {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    public Properties addProperties () {
        Properties properties = new Properties();
        properties.setProperty("javax.persistence.schema-generation.database.action", "none");

        return properties;
    }
}
