package com.azakharov.employeeapp.repository.spring.jdbc

import com.azakharov.employeeapp.repository.jpa.RepositoryException
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

abstract class BaseSpringJdbcRepository<E, ID>(
    protected val jdbcTemplate: JdbcTemplate
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseSpringJdbcRepository::class.java)

        private const val ID_GENERATED_VALUE = "id"
    }

    protected open fun find(sql: String, id: ID): E? =
        jdbcTemplate.queryForObject(sql, this::constructEntity, id)

    protected open fun findAll(sql: String): List<E> =
        jdbcTemplate.query(sql) { resultSet, row -> constructEntity(resultSet, row) }

    protected open fun save(sql: String, entity: E): E =
        GeneratedKeyHolder().let { keyHolder ->
            jdbcTemplate.update(performPreparedStatementForSave(sql, entity), keyHolder)

            keyHolder.key?.toLong().let { id ->
                id.takeIf { it != null }?.let {
                    entity.constructSavedEntity(it)
                } ?: throw SpringJdbcRepositoryException("Generated id after saving is null")
            }
        }

    protected open fun update(sql: String, entity: E): E =
        entity.apply {
            this.convertToParams().let { params ->
                jdbcTemplate.update(sql, params)
            }
        }

    protected open fun delete(sql: String, id: ID) {
        checkAffectedRow(jdbcTemplate.update(sql, id), id)
    }

    /**
     * Converts entity to list of parameters, that will be injected into SQL query.
     *
     * @return list of parameters.
     */
    protected abstract fun E.convertToParams(): List<Any?>

    /**
     * Constructs entity from ResultSet after processing SQL query.
     *
     * @return constructed entity.
     */
    protected abstract fun constructEntity(resultSet: ResultSet, rowNum: Int): E

    /**
     * Constructs entity from generated database id and saved entity.
     *
     * @param id generated database id.
     * @return constructed entity with generated id.
     */
    protected abstract fun E.constructSavedEntity(id: Long): E

    private fun performPreparedStatementForSave(sql: String, entity: E): (connection: Connection) -> PreparedStatement =
        { connection ->
            connection.prepareStatement(sql, arrayOf(ID_GENERATED_VALUE)).apply {
                setParams(this, entity.convertToParams())
            }
        }

    private fun checkAffectedRow(affectedRows: Int, id: ID) {
        if (affectedRows == 0) throw SpringJdbcRepositoryException("There is no record with ID $id")
    }

    private fun setParams(preparedStatement: PreparedStatement, params: List<Any?>) {
        try {
            params.indices.forEach { index ->
                (index + 1).let { sqlIndex -> preparedStatement.setObject(sqlIndex, params[sqlIndex]) }
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during setting params to JDBC prepared statement, message: ${e.message}")
            LOGGER.debug("Exception during setting params to JDBC prepared statement", e)
            throw SpringJdbcRepositoryException("Exception during setting params to JDBC prepared statement, message: ${e.message}")
        }
    }
}

class SpringJdbcRepositoryException(message: String) : RepositoryException(message)
