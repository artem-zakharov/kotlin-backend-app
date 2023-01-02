package com.azakharov.employeeapp.rest.spark.exception

import com.azakharov.employeeapp.domain.exception.InvalidDomainException
import com.azakharov.employeeapp.domain.exception.InvalidTypedIdException
import com.azakharov.employeeapp.rest.spark.util.performJsonResponse
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.ExceptionView
import com.azakharov.employeeapp.service.exception.EmployeePositionNotFoundException
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/exception/SparkExceptionHandler.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/exception/SparkExceptionHandler.java</a>
 */
class SparkExceptionHandler @Inject constructor(
    private val writeJson: (Any) -> String
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(SparkExceptionHandler::class.java)
    }

    fun handleInvalidDomainException() {
        Spark.exception(InvalidDomainException::class.java) { e: InvalidDomainException, _: Request?, response: Response ->
            HttpStatus.PRECONDITION_FAILED_412.performErrorResponse(response, e)
        }
    }

    fun handleInvalidTypedIdException() {
        Spark.exception(InvalidTypedIdException::class.java) { e: InvalidTypedIdException, _: Request?, response: Response ->
            HttpStatus.PRECONDITION_FAILED_412.performErrorResponse(response, e)
        }
    }

    fun handleEmployeeServiceException() {
        Spark.exception(EmployeePositionNotFoundException::class.java) { e: EmployeePositionNotFoundException, _: Request?, response: Response ->
            HttpStatus.PRECONDITION_FAILED_412.performErrorResponse(response, e)
        }
    }

    fun handleIllegalStateException() {
        Spark.exception(IllegalStateException::class.java) { e: IllegalStateException, _: Request?, response: Response ->
            HttpStatus.INTERNAL_SERVER_ERROR_500.performErrorResponse(response, e)
        }
    }

    fun handleUnexpectedException() {
        Spark.exception(RuntimeException::class.java) { e: RuntimeException, _: Request?, response: Response ->
            HttpStatus.INTERNAL_SERVER_ERROR_500.performErrorResponse(response, e)
        }
    }

    private fun Int.performErrorResponse(response: Response, e: Exception) {
        LOGGER.debug("${e.javaClass.name} was handled", e)

        response.performJsonResponse(this)
        response.body(writeJson(ExceptionView(this, e.message!!)))
    }
}
