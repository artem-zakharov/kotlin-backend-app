package com.azakharov.employeeapp.repository.hibernate

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import javax.inject.Inject
import org.hibernate.Session
import org.slf4j.LoggerFactory

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
        val employee = super.find(id)
        LOGGER.trace("EmployeeEntity detailed printing: $employee")

        return employee
    }

    override fun findAll(): List<EmployeeEntity> {
        LOGGER.debug("Finding all EmployeeEntity in database started")
        val employees = super.findAll()
        LOGGER.trace("EmployeeEntity detailed printing: $employees")

        return employees
    }

    override fun save(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity saving started, position: $entity")
        val saved = super.save(entity)
        LOGGER.debug("EmployeeEntity saving successfully ended, generated id: ${saved.id}")

        return saved
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