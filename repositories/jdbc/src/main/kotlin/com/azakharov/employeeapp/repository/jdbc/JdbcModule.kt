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
        private const val ENV_DATASOURCE_URL_KEY = "SPRING_DATASOURCE_URL"
        private const val ENV_DATASOURCE_USERNAME_KEY = "POSTGRES_USER"
        private const val ENV_DATASOURCE_PASSWORD_KEY = "POSTGRES_PASSWORD"
    }

    @Provides
    @Singleton
    fun provideHikariConfig(): HikariConfig {
        val hikariConfig = HikariConfig()

        hikariConfig.jdbcUrl = System.getenv(ENV_DATASOURCE_URL_KEY)
        hikariConfig.username = System.getenv(ENV_DATASOURCE_USERNAME_KEY)
        hikariConfig.password = System.getenv(ENV_DATASOURCE_PASSWORD_KEY)

        return hikariConfig
    }

    @Provides
    @Singleton
    fun provideDataSource(hikariConfig: HikariConfig): DataSource {
        return HikariDataSource(hikariConfig)
    }

    override fun configure() {
        bindJdbcRepositories()
    }

    private fun bindJdbcRepositories() {
        super.bind(EmployeePositionRepository::class.java).to(EmployeePositionJdbcRepository::class.java)
        super.bind(EmployeeRepository::class.java).to(EmployeeJdbcRepository::class.java)
    }
}