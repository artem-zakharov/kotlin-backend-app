package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import org.hibernate.Session
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/EmployeePositionHibernateRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/hibernate/src/main/java/com/azakharov/employeeapp/repository/hibernate/EmployeePositionHibernateRepository.java</a>
 */
class EmployeePositionHibernateRepository @Inject constructor(
    session: Session
) : BaseHibernateRepository<EmployeePositionEntity, Long>(session, EmployeePositionEntity::class.java),
    EmployeePositionRepository {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeePositionHibernateRepository::class.java)
    }

    override fun find(id: Long): EmployeePositionEntity? {
        LOGGER.debug("Finding EmployeePositionEntity started, id: $id")
        return super.find(id).also {
            LOGGER.trace("EmployeePositionEntity detailed printing: $it")
        }
    }

    override fun findAll(): List<EmployeePositionEntity> {
        LOGGER.debug("Finding all EmployeePositionEntities started")
        return super.findAll().also {
            LOGGER.trace("EmployeePositionEntities detailed printing: $it")
        }
    }

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("Saving EmployeePositionEntity started, position: $entity")
        return super.save(entity).also {
            LOGGER.debug("EmployeePositionEntity saving successfully ended, generated id: ${it.id}")
        }
    }

    override fun update(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("EmployeePositionEntity updating started, position: $entity")
        return super.update(entity)
    }

    override fun delete(id: Long) {
        LOGGER.debug("EmployeePositionEntity deleting started, id: $id")
        super.delete(id)
    }
}
