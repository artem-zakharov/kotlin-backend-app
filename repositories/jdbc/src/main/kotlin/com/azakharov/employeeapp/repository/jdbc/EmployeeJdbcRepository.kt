package com.azakharov.employeeapp.repository.jdbc

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import javax.inject.Inject
import javax.sql.DataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Optional

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/EmployeeJdbcRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/EmployeeJdbcRepository.java</a>
 */
class EmployeeJdbcRepository @Inject constructor(dataSource: DataSource) : BaseJdbcRepository<EmployeeEntity, Long>(dataSource),
    EmployeeRepository {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeeJdbcRepository::class.java)

        /** SQL queries  */
        private const val FIND_EMPLOYEE_BY_ID_SQL = "SELECT e.id, e.first_name, e.surname, p.id AS \"p.ID\", " +
                                                    "p.name AS \"p.name\" FROM employees e " +
                                                    "INNER JOIN employee_positions p ON e.position_id = p.id " +
                                                    "WHERE e.id = ?"
        private const val FIND_ALL_EMPLOYEES_SQL = "SELECT e.id, e.first_name, e.surname, p.id AS \"p.id\", " +
                                                   "p.name AS \"p.name\" FROM employees e " +
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

    override fun convertEntityToParams(entity: EmployeeEntity): List<Any?> {
        val params = ArrayList<Any?>()
        params.add(entity.firstName)
        params.add(entity.surname)

        if (entity.positionEntity != null) {
            params.add(entity.positionEntity!!.id)
        }

        if (entity.id != null) {
            params.add(entity.id)
        }

        return params
    }

    override fun constructEntity(resultSet: ResultSet): EmployeeEntity {
        return try {
            val position = EmployeePositionEntity(resultSet.getLong(POSITION_ID_COLUMN),
                                                  resultSet.getString(EMPLOYEE_POSITION_NAME_COLUMN))
            EmployeeEntity(resultSet.getLong(ID_COLUMN),
                           resultSet.getString(EMPLOYEE_FIRST_NAME_COLUMN),
                           resultSet.getString(EMPLOYEE_SURNAME_COLUMN),
                           position)
        } catch (e: SQLException) {
            LOGGER.error("Exception during extracting data from JDBC result set, message: ${e.message}")
            LOGGER.debug("Exception during extracting data from JDBC result set", e)
            throw JdbcRepositoryException("Exception during extracting data from JDBC result set, message: ${e.message}")
        }
    }

    override fun constructSavedEntity(generatedKeys: ResultSet, saved: EmployeeEntity): EmployeeEntity {
        return try {
            val id = generatedKeys.getInt(ID_COLUMN)
            EmployeeEntity(id.toLong(), saved.firstName, saved.surname, saved.positionEntity)
        } catch (e: SQLException) {
            LOGGER.error("Exception during extracting data from JDBC result set, message: ${e.message}")
            LOGGER.debug("Exception during extracting data from JDBC result set", e)
            throw JdbcRepositoryException("Exception during extracting data from JDBC result set, message: ${e.message}")
        }
    }

    override fun find(id: Long): EmployeeEntity? {
        LOGGER.debug("Finding EmployeeEntity in database started for id: $id")
        val employee = super.find(FIND_EMPLOYEE_BY_ID_SQL, id)
        LOGGER.trace("EmployeeEntity detailed printing: $employee")

        return employee
    }

    override fun findAll(): List<EmployeeEntity> {
        LOGGER.debug("Finding all EmployeeEntity in database started")
        val employees = super.findAll(FIND_ALL_EMPLOYEES_SQL)
        LOGGER.trace("EmployeeEntity detailed printing: $employees")

        return employees
    }

    override fun save(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity saving started, position: $ entity")
        val saved = super.save(SAVE_EMPLOYEE_SQL, entity)
        LOGGER.debug("EmployeeEntity saving successfully ended, generated id: ${saved.id}")

        return saved
    }

    override fun update(entity: EmployeeEntity): EmployeeEntity {
        LOGGER.debug("EmployeeEntity updating started, position: $ entity")
        return super.update(UPDATE_EMPLOYEE_SQL, entity)
    }

    override fun delete(id: Long) {
        LOGGER.debug("EmployeeEntity deleting started, id: $id")
        super.delete(DELETE_EMPLOYEE_SQL, id)
    }
}