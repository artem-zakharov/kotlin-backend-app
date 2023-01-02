package com.azakharov.employeeapp.repository.spring.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["com.azakharov.employeeapp.repository.spring.data"])
@EnableTransactionManagement
open class SpringDataConfig {

    private companion object {
        private const val ENTITY_PACKAGE = "com.azakharov.employeeapp.repository.jpa.entity"

        private val ENV_DATASOURCE_URL = System.getenv("SPRING_DATASOURCE_URL")
        private val ENV_DATASOURCE_USERNAME = System.getenv("POSTGRES_USER")
        private val ENV_DATASOURCE_PASSWORD = System.getenv("POSTGRES_PASSWORD")
    }

    @Bean
    open fun hikariConfig(): HikariConfig = HikariConfig().apply {
        jdbcUrl = ENV_DATASOURCE_URL
        username = ENV_DATASOURCE_USERNAME
        password = ENV_DATASOURCE_PASSWORD
    }

    @Bean
    open fun dataSource(hikariConfig: HikariConfig): DataSource = HikariDataSource(hikariConfig)

    @Bean
    open fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            this.jpaVendorAdapter = HibernateJpaVendorAdapter().apply { setGenerateDdl(true) }
            this.dataSource = dataSource

            setPackagesToScan(ENTITY_PACKAGE)
        }

    @Bean
    open fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager =
        JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory
        }
}
