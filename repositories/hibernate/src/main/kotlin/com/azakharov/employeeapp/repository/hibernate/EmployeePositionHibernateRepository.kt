package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import javax.inject.Inject
import org.hibernate.Session
import org.slf4j.LoggerFactory

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
        LOGGER.debug("Finding EmployeePositionEntity in database started for id: $id")
        val position = super.find(id)
        LOGGER.trace("EmployeePositionEntity detailed printing: $position")

        return position
    }

    override fun findAll(): List<EmployeePositionEntity> {
        LOGGER.debug("Finding all EmployeePositionEntity in database started")
        val positions = super.findAll()
        LOGGER.trace("EmployeePositionEntities detailed printing: $positions")

        return positions
    }

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("EmployeePositionEntity saving started, position: $entity")
        val saved = super.save(entity)
        LOGGER.debug("EmployeePositionEntity saving successfully ended, generated id: ${saved.id}")

        return saved
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