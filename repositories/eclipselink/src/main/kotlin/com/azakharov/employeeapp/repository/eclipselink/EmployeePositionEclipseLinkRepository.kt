package com.azakharov.employeeapp.repository.eclipselink

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import javax.inject.Inject
import javax.persistence.EntityManager
import org.slf4j.LoggerFactory

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EmployeePositionEclipseLinkRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/eclipselink/src/main/java/com/azakharov/employeeapp/repository/eclipselink/EmployeePositionEclipseLinkRepository.java</a>
 */
class EmployeePositionEclipseLinkRepository @Inject constructor(
    entityManager: EntityManager
) : BaseEclipseLinkRepository<EmployeePositionEntity, Long>(entityManager, EmployeePositionEntity::class.java),
    EmployeePositionRepository {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeePositionEclipseLinkRepository::class.java)
    }

    override fun find(id: Long): EmployeePositionEntity? {
        LOGGER.debug("Finding EmployeePositionEntity started, id: $id")
        val position = super.find(id)
        LOGGER.trace("EmployeePositionEntity detailed printing: $position")

        return position
    }

    override fun findAll(): List<EmployeePositionEntity> {
        LOGGER.debug("Finding all EmployeePositionEntities started")
        val positions = super.findAll()
        LOGGER.trace("EmployeePositionEntities detailed printing: $positions")

        return positions
    }

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("Saving EmployeePositionEntity started, position: $entity")
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