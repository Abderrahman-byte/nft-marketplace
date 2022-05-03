package org.stibits.rnft.config;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.stibits.rnft.handlers.AuthenticatedOnly;
import org.stibits.rnft.handlers.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("org.stibits.rnft")
@PropertySource("classpath:env.properties")
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    Environment environment;

    @Bean
    public MessageSource messageSource () {
        ReloadableResourceBundleMessageSource messageSource =
            new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

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
        entityManagerFactoryBean.setPackagesToScan("org.stibits.rnft.entities");
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
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        return properties;
    }

    @Bean
    public AuthenticationHandler authenticationHandler () {
        return new AuthenticationHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> authenticatedOnlyPaths = List.of("/api/*/marketplace/create", "/api/*/profile", "/api/*/marketplace/like", "/api/*/user/collections");
        registry.addInterceptor(authenticationHandler()).addPathPatterns("/**").order(0);
        registry.addInterceptor(new AuthenticatedOnly()).addPathPatterns(authenticatedOnlyPaths).order(1);
    }

    // Serving static content just in case of using css
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/media/**").addResourceLocations("file://" + environment.getProperty("file.uploadDir") );
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("http://localhost:3000/").allowCredentials(true).maxAge(3600).allowedMethods("*"); 
    }
}
