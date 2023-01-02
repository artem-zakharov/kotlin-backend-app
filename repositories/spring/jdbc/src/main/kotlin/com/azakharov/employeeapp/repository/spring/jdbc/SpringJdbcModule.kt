package com.azakharov.employeeapp.repository.spring.jdbc

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

class SpringJdbcModule : AbstractModule() {

    private companion object {
        private val ENV_DATASOURCE_URL = System.getenv("SPRING_DATASOURCE_URL")
        private val ENV_DATASOURCE_USERNAME = System.getenv("POSTGRES_USER")
        private val ENV_DATASOURCE_PASSWORD = System.getenv("POSTGRES_PASSWORD")
    }

    @Provides
    @Singleton
    fun provideHikariConfig(): HikariConfig = HikariConfig().apply {
        jdbcUrl = ENV_DATASOURCE_URL
        username = ENV_DATASOURCE_USERNAME
        password = ENV_DATASOURCE_PASSWORD
    }

    @Provides
    @Singleton
    fun dataSource(config: HikariConfig?): DataSource = HikariDataSource(config)

    @Provides
    @Singleton
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate = JdbcTemplate(dataSource)

    override fun configure() {
        bindSpringJdbcRepositories()
    }

    private fun bindSpringJdbcRepositories() {
        super.bind(EmployeePositionRepository::class.java).to(EmployeePositionSpringJdbcRepository::class.java)
        super.bind(EmployeeRepository::class.java).to(EmployeeSpringJdbcRepository::class.java)
    }
}
