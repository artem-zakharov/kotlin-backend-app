package com.azakharov.employeeapp.repository.spring.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(basePackages = ["com.azakharov.employeeapp.repository.spring.data"])
@EnableTransactionManagement
open class SpringDataConfig {

    private companion object {
        private const val ENTITY_PACKAGE = "com.azakharov.employeeapp.repository.jpa.entity"

        private const val ENV_DATASOURCE_URL_KEY = "SPRING_DATASOURCE_URL"
        private const val ENV_DATASOURCE_USERNAME_KEY = "POSTGRES_USER"
        private const val ENV_DATASOURCE_PASSWORD_KEY = "POSTGRES_PASSWORD"
    }

    @Bean
    open fun hikariConfig(): HikariConfig {
        val hikariConfig = HikariConfig()

        hikariConfig.jdbcUrl = System.getenv(ENV_DATASOURCE_URL_KEY)
        hikariConfig.username = System.getenv(ENV_DATASOURCE_USERNAME_KEY)
        hikariConfig.password = System.getenv(ENV_DATASOURCE_PASSWORD_KEY)

        return hikariConfig
    }

    @Bean
    open fun dataSource(hikariConfig: HikariConfig): DataSource {
        return HikariDataSource(hikariConfig)
    }

    @Bean
    open fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val jpaVendorAdapter = HibernateJpaVendorAdapter()
        jpaVendorAdapter.setGenerateDdl(true)

        val factory = LocalContainerEntityManagerFactoryBean()
        factory.jpaVendorAdapter = jpaVendorAdapter
        factory.setPackagesToScan(ENTITY_PACKAGE)
        factory.dataSource = dataSource

        return factory
    }

    @Bean
    open fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val jpaTransactionManager = JpaTransactionManager()
        jpaTransactionManager.entityManagerFactory = entityManagerFactory
        return jpaTransactionManager
    }
}