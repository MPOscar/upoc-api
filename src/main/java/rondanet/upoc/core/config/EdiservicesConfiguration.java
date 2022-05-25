package rondanet.upoc.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "catalogo.sincronizador.core.ediservices.ediservicesRepositories",
        entityManagerFactoryRef = "ediservicesEntityManager",
        transactionManagerRef = "ediservicesTransactionManager")
public class EdiservicesConfiguration {

    @Autowired
    private Environment env;

    @Bean(name = "ediservicesDataSource")
    @ConfigurationProperties(prefix="ediservices.datasource")
    public DataSource ediservicesDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ediservicesEntityManager")
    public LocalContainerEntityManagerFactoryBean ediservicesEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ediservicesDataSource());
        em.setPackagesToScan(
                new String[] { "common.rondanet.clasico.core.ediservices.models" });
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "ediservicesTransactionManager")
    public PlatformTransactionManager ediservicesTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                ediservicesEntityManager().getObject());
        return transactionManager;
    }
}