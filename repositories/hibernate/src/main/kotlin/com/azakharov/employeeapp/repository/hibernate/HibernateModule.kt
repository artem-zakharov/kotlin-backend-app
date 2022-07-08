package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import org.hibernate.Session
import org.hibernate.cfg.Configuration

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/HibernateModule.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/HibernateModule.java</a>
 */
class HibernateModule : AbstractModule() {

    private companion object {
        private const val JDBC_URL_KEY = "hibernate.connection.url"
        private const val JDBC_USERNAME_KEY = "hibernate.connection.username"
        private const val JDBC_PASSWORD_KEY = "hibernate.connection.password"

        private const val ENV_DATASOURCE_URL_KEY = "SPRING_DATASOURCE_URL"
        private const val ENV_DATASOURCE_USERNAME_KEY = "POSTGRES_USER"
        private const val ENV_DATASOURCE_PASSWORD_KEY = "POSTGRES_PASSWORD"
    }

    @Provides
    @Singleton
    fun provideHibernateConfiguration(): Configuration {
        val configuration = Configuration()

        configuration.setProperty(JDBC_URL_KEY, System.getenv(ENV_DATASOURCE_URL_KEY))
        configuration.setProperty(JDBC_USERNAME_KEY, System.getenv(ENV_DATASOURCE_USERNAME_KEY))
        configuration.setProperty(JDBC_PASSWORD_KEY, System.getenv(ENV_DATASOURCE_PASSWORD_KEY))

        return configuration.configure()
    }

    @Provides
    fun provideSession(configuration: Configuration): Session {
        return configuration.buildSessionFactory().openSession()
    }

    override fun configure() {
        bindHibernateRepositories()
    }

    private fun bindHibernateRepositories() {
        super.bind(EmployeeRepository::class.java).to(EmployeeHibernateRepository::class.java)
        super.bind(EmployeePositionRepository::class.java).to(EmployeePositionHibernateRepository::class.java)
    }
}