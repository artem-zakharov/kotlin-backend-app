package com.azakharov.employeeapp.repository.spring.jdbc

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

    protected open fun find(sql: String, id: ID): E? = jdbcTemplate.queryForObject(sql, this::constructEntity, id)

    protected open fun findAll(sql: String): List<E> = jdbcTemplate.query(sql, this::constructEntity)

    protected open fun save(sql: String, entity: E): E {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(performPreparedStatementForSave(sql, entity), keyHolder)

        val id = keyHolder.key?.toLong()
        return if (id != null) constructSavedEntity(id, entity)
        else throw SpringJdbcRepositoryException("Generated id after saving is null")
    }

    protected open fun update(sql: String, entity: E): E {
        val params = convertEntityToParams(entity)
        jdbcTemplate.update(sql, params)

        return entity
    }

    protected open fun delete(sql: String, id: ID) {
        val affectedRows = jdbcTemplate.update(sql, id)
        checkAffectedRow(affectedRows, id)
    }

    /**
     * Converts entity to list of parameters, that will be injected into SQL query.
     *
     * @param entity database entity.
     * @return list of parameters.
     */
    protected abstract fun convertEntityToParams(entity: E): List<Any?>

    /**
     * Constructs entity from ResultSet after processing SQL query.
     *
     * @param resultSet result of SQL query.
     * @return constructed entity.
     */
    protected abstract fun constructEntity(resultSet: ResultSet, rowNum: Int): E

    /**
     * Constructs entity from generated database id and saved entity.
     *
     * @param id generated database id.
     * @param saved entity, that was saved in database.
     * @return constructed entity with generated id.
     */
    protected abstract fun constructSavedEntity(id: Long, saved: E): E

    private fun performPreparedStatementForSave(sql: String, entity: E): (connection: Connection) -> PreparedStatement = {
        connection ->
        val preparedStatement = connection.prepareStatement(sql, arrayOf(ID_GENERATED_VALUE))
        setParams(preparedStatement, convertEntityToParams(entity))
        preparedStatement
    }

    private fun checkAffectedRow(affectedRows: Int, id: ID) {
        if (affectedRows == 0) throw SpringJdbcRepositoryException("There is no record with ID $id")
    }

    private fun setParams(preparedStatement: PreparedStatement, params: List<Any?>) {
        try {
            for (index in params.indices) {
                val sqlQueryIndex = index + 1
                preparedStatement.setObject(sqlQueryIndex, params[index])
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during setting params to JDBC prepared statement, message: ${e.message}")
            LOGGER.debug("Exception during setting params to JDBC prepared statement", e)
            throw SpringJdbcRepositoryException("Exception during setting params to JDBC prepared statement, message: ${e.message}")
        }
    }
}