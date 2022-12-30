package com.azakharov.employeeapp.repository.jdbc

import com.azakharov.employeeapp.repository.jpa.RepositoryException
import java.sql.Connection
import org.slf4j.LoggerFactory
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.Collections
import javax.sql.DataSource

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/BaseJdbcRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/BaseJdbcRepository.java</a>
 */
abstract class BaseJdbcRepository<E : Any, ID : Any>(private val dataSource: DataSource) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseJdbcRepository::class.java)
    }

    protected open fun find(sql: String, id: ID): E? = processSql(sql, listOf(id)) { preparedStatement ->
        resultSetActionForFind().invoke(preparedStatement)
    }

    protected open fun findAll(sql: String): List<E> = mutableListOf<E>().also { entities ->
        processSql(sql) { preparedStatement -> resultSetActionForFindAll(entities).invoke(preparedStatement) }
    }

    protected open fun save(sql: String, entity: E): E =
        processSql(sql, entity.convertToParams(), Statement.RETURN_GENERATED_KEYS) { preparedStatement ->
            resultSetActionForSave(entity).invoke(preparedStatement)
        } ?: throw JdbcRepositoryException("Saved entity can't be null")

    @Suppress("UNCHECKED_CAST")
    protected open fun update(sql: String, entity: E): E = entity.also { entity ->
        entity.convertToParams().let { params ->
            processSql(sql, params) { preparedStatement ->
                resultSetActionForUpdate(params.last() as ID).invoke(preparedStatement)
            }
        }
    }

    protected open fun delete(sql: String, id: ID) {
        processSql(sql, listOf(id)) { preparedStatement -> resultSetActionForUpdate(id).invoke(preparedStatement) }
    }

    private fun <R : Any> processSql(
        sql: String,
        params: List<Any?> = Collections.emptyList(),
        statementKey: Int = Statement.NO_GENERATED_KEYS,
        processResultSet: (input: PreparedStatement) -> R?
    ): R? = try {
        dataSource.connection.use { connection ->
            processPreparedStatement(connection, sql, params, statementKey) { preparedStatement ->
                processResultSet(preparedStatement)
            }
        }
    } catch (e: SQLException) {
        LOGGER.error("Exception during processing SQL query, message: ${e.message}")
        LOGGER.debug("Exception during processing SQL query", e)
        throw JdbcRepositoryException("Exception during processing SQL query, message: ${e.message}")
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
    protected abstract fun ResultSet.constructEntity(): E

    /**
     * Constructs entity from generated database id and saved entity.
     *
     * @param generatedKeys generated database id.
     * @return constructed entity with generated id.
     */
    protected abstract fun E.constructSavedEntity(generatedKeys: ResultSet): E

    private fun resultSetActionForFind(): (preparedStatement: PreparedStatement) -> E? = { preparedStatement ->
        executeResultSetActionForFind(preparedStatement)
    }

    private fun resultSetActionForFindAll(resultList: MutableList<E>): (preparedStatement: PreparedStatement) -> Unit =
        { preparedStatement ->
            executeResultSetActionForFindAll(preparedStatement, resultList)
        }

    private fun resultSetActionForSave(entity: E): (preparedStatement: PreparedStatement) -> E = { preparedStatement ->
        executeResultSetActionForSave(preparedStatement, entity)
    }

    private fun resultSetActionForUpdate(id: ID): (preparedStatement: PreparedStatement) -> Unit =
        { preparedStatement ->
            executeResultSetActionForUpdate(preparedStatement, id)
        }

    private fun executeResultSetActionForFind(preparedStatement: PreparedStatement): E? =
        try {
            preparedStatement.executeQuery().use { resultSet ->
                resultSet.next().takeIf { it }?.let { resultSet.constructEntity() }
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for findById, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for findById action", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for findById, message: ${e.message}")
        }

    private fun executeResultSetActionForFindAll(preparedStatement: PreparedStatement, resultList: MutableList<E>) {
        try {
            preparedStatement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    resultList.add(resultSet.constructEntity())
                }
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for findAll, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for findAll", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for findAll, message: ${e.message}")
        }
    }

    private fun executeResultSetActionForSave(preparedStatement: PreparedStatement, entity: E): E =
        try {
            preparedStatement.executeUpdate()

            preparedStatement.generatedKeys.use { generatedKeys ->
                generatedKeys.next().takeIf { it }?.let {
                    entity.constructSavedEntity(generatedKeys)
                } ?: throw JdbcRepositoryException("ResultSet for save action is empty")
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during performing JDBC ResultSet for save, message: ${e.message}")
            LOGGER.debug("Exception during performing JDBC ResultSet for save", e)
            throw JdbcRepositoryException("Exception during performing JDBC ResultSet for save, message: ${e.message}")
        }

    private fun executeResultSetActionForUpdate(preparedStatement: PreparedStatement, id: ID) {
        try {
            preparedStatement.executeUpdate().takeIf { it == 0 }.let {
                throw JdbcRepositoryException("There is no record with ID $id")
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during executing JDBC preparedStatement for update, message: ${e.message}")
            LOGGER.debug("Exception during executing JDBC preparedStatement for update", e)
            throw JdbcRepositoryException("Exception during executing JDBC preparedStatement for update, message: ${e.message}")
        }
    }

    private fun <R : Any> processPreparedStatement(
        connection: Connection,
        sql: String,
        params: List<Any?>,
        statementKey: Int,
        processResultSet: (input: PreparedStatement) -> R?
    ): R? = connection.prepareStatement(sql, statementKey).use { preparedStatement ->
        setParams(preparedStatement, params)
        processResultSet(preparedStatement)
    }

    private fun setParams(preparedStatement: PreparedStatement, params: List<Any?>) {
        try {
            params.indices.forEach { index ->
                (index + 1).let { sqlIndex -> preparedStatement.setObject(sqlIndex, params[sqlIndex]) }
            }
        } catch (e: SQLException) {
            LOGGER.error("Exception during setting params to JDBC prepared statement, message: ${e.message}")
            LOGGER.debug("Exception during setting params to JDBC prepared statement", e)
            throw JdbcRepositoryException("Exception during setting params to JDBC prepared statement, message: ${e.message}")
        }
    }
}

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcRepositoryException.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcRepositoryException.java</a>
 */
class JdbcRepositoryException(message: String) : RepositoryException(message)
