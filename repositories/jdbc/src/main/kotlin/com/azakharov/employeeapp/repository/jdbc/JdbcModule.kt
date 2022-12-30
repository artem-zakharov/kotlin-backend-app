package com.azakharov.employeeapp.repository.jdbc

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcModule.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcModule.java</a>
 */
class JdbcModule : AbstractModule() {

    companion object {
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
    fun provideDataSource(hikariConfig: HikariConfig): DataSource = HikariDataSource(hikariConfig)

    override fun configure() {
        bindJdbcRepositories()
    }

    private fun bindJdbcRepositories() {
        super.bind(EmployeePositionRepository::class.java).to(EmployeePositionJdbcRepository::class.java)
        super.bind(EmployeeRepository::class.java).to(EmployeeJdbcRepository::class.java)
    }
}