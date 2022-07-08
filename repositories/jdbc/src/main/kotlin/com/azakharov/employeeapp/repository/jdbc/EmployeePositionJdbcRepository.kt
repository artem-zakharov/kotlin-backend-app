package com.azakharov.employeeapp.repository.jdbc

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import javax.inject.Inject
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/EmployeePositionJdbcRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/EmployeePositionJdbcRepository.java</a>
 */
class EmployeePositionJdbcRepository @Inject constructor (dataSource: DataSource) :
    BaseJdbcRepository<EmployeePositionEntity, Long>(dataSource), EmployeePositionRepository {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EmployeePositionJdbcRepository::class.java)

        /** SQL queries  */
        private const val FIND_EMPLOYEE_POSITION_BY_ID_SQL = "SELECT id, name FROM employee_positions WHERE ID = ?"
        private const val FIND_ALL_EMPLOYEE_POSITIONS_SQL = "SELECT id, name FROM employee_positions"
        private const val SAVE_EMPLOYEE_POSITION_SQL = "INSERT INTO employee_positions (name) VALUES (?)"
        private const val UPDATE_EMPLOYEE_POSITION_SQL = "UPDATE employee_positions SET name = ? WHERE id = ?"
        private const val DELETE_EMPLOYEE_POSITION_SQL = "DELETE FROM employee_positions WHERE id = ?"

        /** employee_positions column names  */
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
    }

    override fun convertEntityToParams(entity: EmployeePositionEntity): List<Any?> {
        val params = ArrayList<Any?>()

        params.add(entity.name)
        if (entity.id != null) {
            params.add(entity.id)
        }

        return params
    }

    override fun constructEntity(resultSet: ResultSet): EmployeePositionEntity {
        return try {
            EmployeePositionEntity(resultSet.getLong(ID_COLUMN), resultSet.getString(NAME_COLUMN))
        } catch (e: SQLException) {
            LOGGER.error("Exception during extracting data from JDBC result set, message: ${e.message}")
            LOGGER.debug("Exception during extracting data from JDBC result set", e)
            throw JdbcRepositoryException("Exception during extracting data from JDBC result set, message: ${e.message}")
        }
    }

    override fun constructSavedEntity(generatedKeys: ResultSet, saved: EmployeePositionEntity): EmployeePositionEntity {
        return try {
            val id = generatedKeys.getInt(ID_COLUMN)
            EmployeePositionEntity(id.toLong(), saved.name)
        } catch (e: SQLException) {
            LOGGER.error("Exception during extracting data from JDBC result set, message: ${e.message}")
            LOGGER.debug("Exception during extracting data from JDBC result set", e)
            throw JdbcRepositoryException("Exception during extracting data from JDBC result set, message: ${e.message}")
        }
    }

    override fun find(id: Long): EmployeePositionEntity? {
        LOGGER.debug("Finding EmployeePositionEntity in database started for id: $id")
        val position = super.find(FIND_EMPLOYEE_POSITION_BY_ID_SQL, id)
        LOGGER.trace("EmployeePositionEntity detailed printing: $position")

        return position
    }

    override fun findAll(): List<EmployeePositionEntity> {
        LOGGER.debug("Finding all EmployeePositionEntity in database started")
        val positions = super.findAll(FIND_ALL_EMPLOYEE_POSITIONS_SQL)
        LOGGER.trace("EmployeePositionEntities detailed printing: $positions")

        return positions
    }

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("EmployeePositionEntity saving started, position: $entity")
        val saved = super.save(SAVE_EMPLOYEE_POSITION_SQL, entity)
        LOGGER.debug("EmployeePositionEntity saving successfully ended, generated id: ${saved.id}")

        return saved
    }

    override fun update(entity: EmployeePositionEntity): EmployeePositionEntity {
        LOGGER.debug("EmployeePositionEntity updating started, position: $entity")
        return super.update(UPDATE_EMPLOYEE_POSITION_SQL, entity)
    }

    override fun delete(id: Long) {
        LOGGER.debug("EmployeePositionEntity deleting started, id: $id")
        super.delete(DELETE_EMPLOYEE_POSITION_SQL, id)
    }
}