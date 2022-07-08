package com.azakharov.employeeapp.repository.eclipselink

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

/**
 * Kotlin Copy of
 * <a href=""></a>
 */
class EclipseLinkModule : AbstractModule() {

    private companion object {
        private const val PERSISTENCE_UNIT_NAME = "postgres-pu"

        private const val JDBC_URL_KEY = "javax.persistence.jdbc.url"
        private const val JDBC_USERNAME_KEY = "javax.persistence.jdbc.user"
        private const val JDBC_PASSWORD_KEY = "javax.persistence.jdbc.password"

        private const val ENV_DATASOURCE_URL_KEY = "SPRING_DATASOURCE_URL"
        private const val ENV_DATASOURCE_USERNAME_KEY = "POSTGRES_USER"
        private const val ENV_DATASOURCE_PASSWORD_KEY = "POSTGRES_PASSWORD"
    }

    @Provides
    fun provideEntityManagerFactory(): EntityManagerFactory {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, provideDataSource())
    }

    @Provides
    @Singleton
    fun provideEntityManager(entityManagerFactory: EntityManagerFactory): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    override fun configure() {
        bindEclipseLinkRepositories()
    }

    private fun bindEclipseLinkRepositories() {
        super.bind(EmployeePositionRepository::class.java).to(EmployeePositionEclipseLinkRepository::class.java)
        super.bind(EmployeeRepository::class.java).to(EmployeeEclipseLinkRepository::class.java)
    }

    private fun provideDataSource(): Map<String, String> {
        return hashMapOf(JDBC_URL_KEY to System.getenv(ENV_DATASOURCE_URL_KEY),
                         JDBC_USERNAME_KEY to System.getenv(ENV_DATASOURCE_USERNAME_KEY),
                         JDBC_PASSWORD_KEY to System.getenv(ENV_DATASOURCE_PASSWORD_KEY))
    }
}