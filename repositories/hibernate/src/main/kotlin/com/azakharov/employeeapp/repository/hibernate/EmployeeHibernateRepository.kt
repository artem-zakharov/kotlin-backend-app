package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import org.hibernate.Session
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/EmployeeHibernateRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/EmployeeHibernateRepository.java</a>
 */
class EmployeeHibernateRepository @Inject constructor(
    session: Session
) : BaseHibernateRepository<EmployeeEntity, Long>(session, EmployeeEntity::class.java), EmployeeRepository {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeeHibernateRepository::class.java)
    }

    override fun find(id: Long): EmployeeEntity? {
        LOGGER.debug("Finding EmployeeEntity in database started for id: $id")
        return super.find(id).also {
            LOGGER.trace("EmployeeEntity detailed printing: $it")
        }
    }

    override fun findAll(): List<EmployeeEntity> {
        LOGGER.debug("Finding all EmployeeEntity in database started")
        return super.findAll().also {
            LOGGER.trace("EmployeeEntity detailed printing: $it")
        }
    }

    override fun save(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity saving started, position: $entity")
        return super.save(entity).also {
            LOGGER.debug("EmployeeEntity saving successfully ended, generated id: ${it.id}")
        }
    }

    override fun update(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity updating started, position: $entity")
        return super.update(entity)
    }

    override fun delete(id: Long) {
        LOGGER.debug("EmployeeEntity deleting started, id: $id")
        super.delete(id)
    }
}
