package com.azakharov.employeeapp.repository.eclipselink

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.persistence.EntityManager

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
