package com.azakharov.employeeapp.rest.spark.proxy

import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.ExceptionView
import com.google.common.net.MediaType
import org.eclipse.jetty.http.HttpStatus
import spark.Request
import spark.Response
import spark.Route
import spark.Spark
import java.text.MessageFormat

abstract class BaseSparkRestController<DTO, V : Any>(
    private val jsonUtil: JsonUtil
) {

    protected companion object {
        protected const val ID_ENDPOINT_PARAM = ":id"

        private const val DELETE_SUCCESS_MESSAGE_TEMPLATE = "{0} with ID {1} was deleted"
        private const val PAGE_NOT_FOUND_PATTERN = "*"
        private const val DOMAIN_NOT_FOUND_MESSAGE_TEMPLATE = "{0} with ID {1} wasn''t found"
    }

    protected fun performGetViewEndpointLogic(endpointExecution: (id: Long) -> V?,
                                              gettingDomainName: String): Route {
        return Route { request: Request, response: Response ->

            val id = request.params(ID_ENDPOINT_PARAM).toLong()
            val domain = endpointExecution(id)

            response.status(HttpStatus.OK_200)
            response.type(MediaType.JSON_UTF_8.type())

            if (domain != null) jsonUtil.write(domain)
            else MessageFormat.format(DOMAIN_NOT_FOUND_MESSAGE_TEMPLATE, gettingDomainName, id)
        }
    }

    protected fun performGetAllViewsEndpointLogic(endpointExecution: () -> List<V>): Route {
        return Route { _: Request, response: Response ->

            val domain = endpointExecution()

            response.status(HttpStatus.OK_200)
            response.type(MediaType.JSON_UTF_8.type())

            jsonUtil.write(domain)
        }
    }

    protected fun performUpsertEndpointLogic(endpointExecution: (dto: DTO) -> V,
                                             dtoClass: Class<DTO>): Route {
        return Route { request: Request, response: Response ->

            val savingDto = jsonUtil.read(request.body(), dtoClass)
            val savedView = endpointExecution(savingDto)

            response.status(HttpStatus.OK_200)
            response.type(MediaType.JSON_UTF_8.type())

            jsonUtil.write(savedView)
        }
    }

    protected fun performDeleteDomainEndpointLogic(endpointExecution: (id: Long) -> Unit,
                                                   deletingDomainName: String): Route {
        return Route { request: Request, response: Response ->

            val id = request.params(ID_ENDPOINT_PARAM).toLong()
            endpointExecution(id)

            response.status(HttpStatus.OK_200)
            response.type(MediaType.JSON_UTF_8.type())

            MessageFormat.format(DELETE_SUCCESS_MESSAGE_TEMPLATE, deletingDomainName, id)
        }
    }

    fun performNotFoundEndpoints() {
        Spark.get(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.post(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.delete(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.put(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.patch(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
    }

    private fun performPageNotFoundEndpointLogic(): Route {
        return Route { _: Request?, response: Response ->
            response.status(HttpStatus.NOT_FOUND_404)

            val exceptionView = ExceptionView(HttpStatus.NOT_FOUND_404, "Page not found")
            jsonUtil.write(exceptionView)
        }
    }
}