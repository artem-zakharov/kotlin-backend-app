package com.azakharov.employeeapp.repository.hibernate

import org.hibernate.Session
import org.slf4j.LoggerFactory

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/BaseHibernateRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/BaseHibernateRepository.java</a>
 */
abstract class BaseHibernateRepository<E, ID>(
    protected val session: Session,
    private val entityClass: Class<E>
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseHibernateRepository::class.java)
    }

    protected open fun find(id: ID): E? = session.find(entityClass, id)

    protected open fun findAll(): List<E> {
        val criteriaBuilder = session.criteriaBuilder
        val query = criteriaBuilder.createQuery(entityClass)
        val rootEntry = query.from(entityClass)
        val selectAllQuery = query.select(rootEntry)

        return session.createQuery(selectAllQuery).resultList
    }

    protected open fun save(entity: E): E = upsert(entity)

    protected open fun update(entity: E): E = upsert(entity)

    protected open fun delete(id: ID) {
        val entity = find(id)
        if (entity != null) processDelete(entity) else processDeletingFailing(id)
    }

    @Suppress("UNCHECKED_CAST")
    private fun upsert(entity: E): E {
        session.transaction.begin()
        val saved = session.merge(entity)
        session.transaction.commit()

        return saved as E
    }

    private fun processDelete(entity: E) {
        session.transaction.begin()
        session.remove(entity)
        session.transaction.commit()
    }

    private fun processDeletingFailing(id: ID) {
        LOGGER.debug("Entity with id $id wasn't found for deleting in database")
        throw HibernateRepositoryException("Entity with id $id wasn't found for deleting in database")
    }
}