package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.RepositoryException
import org.hibernate.Session
import org.slf4j.LoggerFactory

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/BaseHibernateRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/BaseHibernateRepository.java</a>
 */
abstract class BaseHibernateRepository<E, ID>(
    private val session: Session,
    private val entityClass: Class<E>
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseHibernateRepository::class.java)
    }

    protected open fun find(id: ID): E? = session.find(entityClass, id)

    protected open fun findAll(): List<E> =
        session.criteriaBuilder.let { builder ->
            builder.createQuery(entityClass).let { query ->
                query.from(entityClass).let { rootEntry ->
                    session.createQuery(query.select(rootEntry)).resultList
                }
            }
        }

    protected open fun save(entity: E): E = upsert(entity)

    protected open fun update(entity: E): E = upsert(entity)

    protected open fun delete(id: ID) = find(id)?.let { processDelete(it) } ?: processDeletingFailing(id)

    @Suppress("UNCHECKED_CAST")
    private fun upsert(entity: E): E = session.run {
        transaction.begin()
        merge(entity).also {
            transaction.commit()
        } as E
    }

    private fun processDelete(entity: E) = session.run {
        transaction.begin()
        remove(entity)
        transaction.commit()
    }

    private fun processDeletingFailing(id: ID) {
        LOGGER.debug("Entity with id $id wasn't found for deleting in database")
        throw HibernateRepositoryException("Entity with id $id wasn't found for deleting in database")
    }
}

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/HibernateRepositoryException.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/HibernateRepositoryException.java</a>
 */
class HibernateRepositoryException(message: String) : RepositoryException(message)
