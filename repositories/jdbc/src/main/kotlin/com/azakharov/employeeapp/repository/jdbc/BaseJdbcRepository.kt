package com.azakharov.employeeapp.repository.jdbc

import javax.sql.DataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.Collections
import java.util.Optional

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/BaseJdbcRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/BaseJdbcRepository.java</a>
 */
abstract class BaseJdbcRepository<E: Any, ID: Any> (protected val dataSource: DataSource) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseJdbcRepository::class.java)
    }

    protected open fun find(sql: String, id: ID): E? = processSql(sql, resultSetActionForFind(), listOf(id))

    protected open fun findAll(sql: String): List<E> {
        val entities: MutableList<E> = mutableListOf()
        processSql(sql, resultSetActionForFindAll(entities))

        return entities
    }

    protected open fun save(sql: String, entity: E): E = processSql(sql,
                                                                    resultSetActionForSave(entity),
                                                                    convertEntityToParams(entity),
                                                                    Statement.RETURN_GENERATED_KEYS)
                                                         ?: throw JdbcRepositoryException("Saved entity can''t be null")

    protected open fun update(sql: String, entity: E): E {
        val params = convertEntityToParams(entity)
        processSql(sql, resultSetActionForUpdate(params.last() as ID), params)

        return entity
    }

    protected open fun delete(sql: String, id: ID) {
        processSql(sql, resultSetActionForUpdate(id), listOf(id))
    }

    protected fun <R: Any> processSql(sql: String,
                                      resultSetExecutor: (input: PreparedStatement) -> R?,
                                      params: List<Any?> = Collections.emptyList(),
                                      statementKey: Int = Statement.NO_GENERATED_KEYS): R? {
        return try {
            dataSource.connection.use { connection ->
                processPreparedStatement(connection, sql, params, resultSetExecutor, statementKey)
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during processing SQL query, message: ${e.message}")
            LOGGER.debug("Exception during processing SQL query", e)
            throw JdbcRepositoryException("Exception during processing SQL query, message: ${e.message}")
        }
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
    protected abstract fun constructEntity(resultSet: ResultSet): E

    /**
     * Constructs entity from generated database id and saved entity.
     *
     * @param generatedKeys generated database id.
     * @param saved entity, that was saved in database.
     * @return constructed entity with generated id.
     */
    protected abstract fun constructSavedEntity(generatedKeys: ResultSet, saved: E): E

    private fun resultSetActionForFind(): (preparedStatement: PreparedStatement) -> E? = {
        preparedStatement -> executeResultSetActionForFind(preparedStatement)
    }

    private fun resultSetActionForFindAll(resultList: MutableList<E>): (preparedStatement: PreparedStatement) -> Unit = {
        preparedStatement -> executeResultSetActionForFindAll(preparedStatement, resultList)
    }

    private fun resultSetActionForSave(entity: E): (preparedStatement: PreparedStatement) -> E = {
        preparedStatement -> executeResultSetActionForSave(preparedStatement, entity)
    }

    private fun resultSetActionForUpdate(id: ID): (preparedStatement: PreparedStatement) -> Unit = {
        preparedStatement -> executeResultSetActionForUpdate(preparedStatement, id)
    }

    private fun executeResultSetActionForFind(preparedStatement: PreparedStatement): E? {
        return try {
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) constructEntity(resultSet) else null
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for findById, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for findById action", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for findById, message: ${e.message}")
        }
    }

    private fun executeResultSetActionForFindAll(preparedStatement: PreparedStatement, resultList: MutableList<E>) {
        try {
            preparedStatement.executeQuery().use { resultSet ->

                while (resultSet.next()) {
                    resultList.add(constructEntity(resultSet))
                }

            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for findAll, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for findAll", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for findAll, message: ${e.message}")
        }
    }

    private fun executeResultSetActionForSave(preparedStatement: PreparedStatement, entity: E): E {
        return try {

            preparedStatement.executeUpdate()
            preparedStatement.generatedKeys.use { generatedKeys ->

                if (generatedKeys.next()) {
                    constructSavedEntity(generatedKeys, entity)
                } else {
                    throw JdbcRepositoryException("ResultSet for save action is empty")
                }

            }

        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for save, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for save", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for save, message: ${e.message}")
        }
    }

    private fun executeResultSetActionForUpdate(preparedStatement: PreparedStatement, id: ID) {
        try {

            val rowsAffected = preparedStatement.executeUpdate()
            if (rowsAffected == 0) {
                throw JdbcRepositoryException("There is no record with ID $id")
            }

        } catch (e: SQLException) {
            LOGGER.error("Exception during executing JDBC preparedStatement for update, message: ${e.message}")
            LOGGER.debug("Exception during executing JDBC preparedStatement for update", e)
            throw JdbcRepositoryException("Exception during executing JDBC preparedStatement for update, message: ${e.message}")
        }
    }

    private fun <R: Any> processPreparedStatement(connection: Connection,
                                                  sql: String,
                                                  params: List<Any?>,
                                                  resultSetExecutor: (input: PreparedStatement) -> R?,
                                                  statementKey: Int) : R? {

        connection.prepareStatement(sql, statementKey).use { preparedStatement ->
            setParams(preparedStatement, params)
            return resultSetExecutor(preparedStatement)
        }

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
            throw JdbcRepositoryException("Exception during setting params to JDBC prepared statement, message: ${e.message}")
        }
    }
}