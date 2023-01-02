package com.azakharov.employeeapp.repository.spring.jdbc

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet
import java.sql.SQLException
import javax.inject.Inject

class EmployeeSpringJdbcRepository @Inject constructor(
    jdbcTemplate: JdbcTemplate
) : BaseSpringJdbcRepository<EmployeeEntity, Long>(jdbcTemplate), EmployeeRepository {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeeSpringJdbcRepository::class.java)

        /** SQL queries  */
        private const val FIND_EMPLOYEE_BY_ID_SQL = "SELECT e.id, e.first_name, e.surname, " +
            "p.id AS \"p.id\", p.name AS \"p.name\" " +
            "FROM employees e INNER JOIN employee_positions p " +
            "ON e.position_id = p.id WHERE e.id = ?"
        private const val FIND_ALL_EMPLOYEES_SQL = "SELECT e.id, e.first_name, e.surname, p.id AS \"p.id\", p.name " +
            "AS \"p.name\" FROM employees e " +
            "INNER JOIN employee_positions p ON e.position_id = p.id"
        private const val SAVE_EMPLOYEE_SQL = "INSERT INTO employees (first_name, surname, position_id)" +
            "VALUES (?, ?, ?)"
        private const val UPDATE_EMPLOYEE_SQL = "UPDATE employees SET first_name = ?, surname = ?, position_id = ? " +
            "WHERE id = ?"
        private const val DELETE_EMPLOYEE_SQL = "DELETE FROM employees WHERE id = ?"

        /** employees and employee_positions column names  */
        private const val ID_COLUMN = "id"
        private const val POSITION_ID_COLUMN = "p.$ID_COLUMN"
        private const val EMPLOYEE_FIRST_NAME_COLUMN = "first_name"
        private const val EMPLOYEE_SURNAME_COLUMN = "surname"
        private const val EMPLOYEE_POSITION_NAME_COLUMN = "p.name"
    }

    override fun EmployeeEntity.convertToParams(): List<Any?> = ArrayList<Any?>().apply {
        add(firstName)
        add(surname)

        id.takeIf { it != null }?.let { add(it) }
        positionEntity.takeIf { it != null }?.let { add(it.id) }
    }

    override fun constructEntity(resultSet: ResultSet, rowNum: Int): EmployeeEntity = try {
        EmployeeEntity(
            resultSet.getLong(ID_COLUMN),
            resultSet.getString(EMPLOYEE_FIRST_NAME_COLUMN),
            resultSet.getString(EMPLOYEE_SURNAME_COLUMN),
            EmployeePositionEntity(
                resultSet.getLong(POSITION_ID_COLUMN),
                resultSet.getString(EMPLOYEE_POSITION_NAME_COLUMN)
            )
        )
    } catch (e: SQLException) {
        LOGGER.error("Exception during extracting data from JDBC result set, message: {}", e.message)
        LOGGER.debug("Exception during extracting data from JDBC result set", e)
        throw SpringJdbcRepositoryException("Exception during extracting data from JDBC result set, message: ${e.message}")
    }

    override fun EmployeeEntity.constructSavedEntity(id: Long): EmployeeEntity = this.copy(id = id)

    override fun find(id: Long): EmployeeEntity? {
        LOGGER.debug("Finding EmployeeEntity in database started for id: $id")
        val employee = find(FIND_EMPLOYEE_BY_ID_SQL, id)
        LOGGER.trace("EmployeeEntity detailed printing: $employee")

        return employee
    }

    override fun findAll(): List<EmployeeEntity> {
        LOGGER.debug("Finding all EmployeeEntity in database started")
        val employees = findAll(FIND_ALL_EMPLOYEES_SQL)
        LOGGER.trace("EmployeeEntity detailed printing: $employees")

        return employees
    }

    override fun save(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity saving started, position: $entity")
        val saved = save(SAVE_EMPLOYEE_SQL, entity)
        LOGGER.debug("EmployeeEntity saving successfully ended, generated id: ${saved.id}")

        return saved
    }

    override fun update(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity updating started, position: $entity")
        return super.update(UPDATE_EMPLOYEE_SQL, entity)
    }

    override fun delete(id: Long) {
        LOGGER.debug("EmployeeEntity deleting started, id: $id")
        super.delete(DELETE_EMPLOYEE_SQL, id)
    }
}
