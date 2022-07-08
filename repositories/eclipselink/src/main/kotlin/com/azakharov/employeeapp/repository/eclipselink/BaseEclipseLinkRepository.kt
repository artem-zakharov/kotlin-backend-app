package com.azakharov.employeeapp.repository.eclipselink

import javax.persistence.EntityManager
import org.slf4j.LoggerFactory

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkModule.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EclipseLinkModule.java</a>
 */
abstract class BaseEclipseLinkRepository<E, ID>(
    protected val entityManager: EntityManager,
    private val entityClass: Class<E>
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseEclipseLinkRepository::class.java)
    }

    protected open fun find(id: ID): E? = entityManager.find(entityClass, id)

    protected open fun findAll(): List<E> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val query = criteriaBuilder.createQuery(entityClass)
        val rootEntry = query.from(entityClass)
        val selectAllQuery = query.select(rootEntry)

        return entityManager.createQuery(selectAllQuery).resultList
    }

    protected open fun save(entity: E): E = upsert(entity)

    protected open fun update(entity: E): E = upsert(entity)

    protected open fun delete(id: ID) {
        val entity = find(id)
        if (entity != null) processDelete(entity) else processDeletingFailing(id)
    }

    private fun upsert(entity: E): E {
        entityManager.transaction.begin()
        val saved = entityManager.merge(entity)
        entityManager.transaction.commit()

        return saved
    }

    private fun processDelete(entity: E) {
        entityManager.transaction.begin()
        entityManager.remove(entity)
        entityManager.transaction.commit()
    }

    private fun processDeletingFailing(id: ID) {
        LOGGER.debug("Entity with id $id wasn't found for deleting in database")
        throw EclipseLinkRepositoryException("Entity with id $id wasn't found for deleting in database")
    }
}