package com.azakharov.employeeapp.rest.spark.exception

import com.azakharov.employeeapp.domain.exception.InvalidDomainException
import com.azakharov.employeeapp.domain.exception.InvalidTypedIdException
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.ExceptionView
import com.azakharov.employeeapp.service.exception.EmployeePositionNotFoundException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.google.common.net.MediaType
import javax.inject.Inject
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/exception/SparkExceptionHandler.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/exception/SparkExceptionHandler.java</a>
 */
class SparkExceptionHandler @Inject constructor(
    private val jsonUtil: JsonUtil
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(SparkExceptionHandler::class.java)
    }

    fun handleInvalidDomainException() {
        Spark.exception(InvalidDomainException::class.java) {
            e: InvalidDomainException, _: Request?, response: Response ->
            LOGGER.debug("InvalidDomainException was handled", e)

            response.status(HttpStatus.PRECONDITION_FAILED_412)
            response.type(MediaType.JSON_UTF_8.type())

            val exceptionView = ExceptionView(HttpStatus.PRECONDITION_FAILED_412, e.message!!)
            response.body(jsonUtil.write(exceptionView))
        }
    }

    fun handleInvalidTypedIdException() {
        Spark.exception(InvalidTypedIdException::class.java) {
            e: InvalidTypedIdException, _: Request?, response: Response ->
            LOGGER.debug("InvalidTypedIdException was handled", e)

            response.status(HttpStatus.PRECONDITION_FAILED_412)
            response.type(MediaType.JSON_UTF_8.type())

            val exceptionView = ExceptionView(HttpStatus.PRECONDITION_FAILED_412, e.message!!)
            response.body(jsonUtil.write(exceptionView))
        }
    }

    fun handleEmployeeServiceException() {
        Spark.exception(EmployeePositionNotFoundException::class.java) {
            e: EmployeePositionNotFoundException, _: Request?, response: Response ->
            LOGGER.debug("EmployeePositionNotFoundException was handled", e)

            response.status(HttpStatus.PRECONDITION_FAILED_412)
            response.type(MediaType.JSON_UTF_8.type())

            val exceptionView = ExceptionView(HttpStatus.PRECONDITION_FAILED_412, e.message!!)
            response.body(jsonUtil.write(exceptionView))
        }
    }

    fun handleIllegalStateException() {
        Spark.exception(IllegalStateException::class.java) {
            e: IllegalStateException, _: Request?, response: Response ->
            LOGGER.debug("IllegalStateException was handled", e)

            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
            response.type(MediaType.JSON_UTF_8.type())

            val exceptionView = ExceptionView(HttpStatus.PRECONDITION_FAILED_412, e.message!!)
            response.body(jsonUtil.write(exceptionView))
        }
    }

    fun handleUnexpectedException() {
        Spark.exception(RuntimeException::class.java) {
            e: RuntimeException, _: Request?, response: Response ->
            LOGGER.debug("Unexpected exception was handled", e)

            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
            response.type(MediaType.JSON_UTF_8.type())

            val exceptionView = ExceptionView(HttpStatus.INTERNAL_SERVER_ERROR_500, e.message!!)
            response.body(jsonUtil.write(exceptionView))
        }
    }
}