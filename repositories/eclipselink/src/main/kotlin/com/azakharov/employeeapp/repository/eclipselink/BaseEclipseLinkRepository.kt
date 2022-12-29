package com.azakharov.employeeapp.repository.eclipselink

import com.azakharov.employeeapp.repository.jpa.RepositoryException
import org.slf4j.LoggerFactory
import javax.persistence.EntityManager

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkModule.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkModule.java</a>
 */
abstract class BaseEclipseLinkRepository<E, ID>(
    private val entityManager: EntityManager,
    private val entityClass: Class<E>
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseEclipseLinkRepository::class.java)
    }

    protected open fun find(id: ID): E? = entityManager.find(entityClass, id)

    protected open fun findAll(): List<E> =
        entityManager.criteriaBuilder.let { builder ->
            builder.createQuery(entityClass).let { query ->
                query.from(entityClass).let { rootEntry ->
                    entityManager.createQuery(query.select(rootEntry)).resultList
                }
            }
        }

    protected open fun save(entity: E): E = upsert(entity)

    protected open fun update(entity: E): E = upsert(entity)

    protected open fun delete(id: ID) = find(id)?.let { processDelete(it) } ?: processDeletingFailing(id)

    private fun upsert(entity: E): E = entityManager.run {
        transaction.begin()
        merge(entity).also {
            transaction.commit()
        }
    }

    private fun processDelete(entity: E) = entityManager.run {
        transaction.begin()
        remove(entity)
        transaction.commit()
    }

    private fun processDeletingFailing(id: ID) {
        LOGGER.debug("Entity with id $id wasn't found for deleting in database")
        throw EclipseLinkRepositoryException("Entity with id $id wasn't found for deleting in database")
    }
}

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkRepositoryException.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkRepositoryException.java</a>
 */
class EclipseLinkRepositoryException(message: String) : RepositoryException(message)
