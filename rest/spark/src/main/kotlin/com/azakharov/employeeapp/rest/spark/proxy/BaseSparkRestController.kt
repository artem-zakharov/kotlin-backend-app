package com.azakharov.employeeapp.rest.spark.proxy

import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.ExceptionView
import com.google.common.net.MediaType
import org.eclipse.jetty.http.HttpStatus
import spark.Request
import spark.Response
import spark.Route
import spark.Spark

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/BaseSparkRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/BaseSparkRestController.java</a>
 */
abstract class BaseSparkRestController<DTO, V : Any>(
    private val jsonUtil: JsonUtil
) {

    protected companion object {
        protected const val ID_ENDPOINT_PARAM = ":id"
        private const val PAGE_NOT_FOUND_PATTERN = "*"
    }

    fun performNotFoundEndpoints() {
        Spark.get(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.post(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.delete(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.put(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
        Spark.patch(PAGE_NOT_FOUND_PATTERN, performPageNotFoundEndpointLogic())
    }

    protected fun performGetViewEndpointLogic(gettingDomainName: String, executeEndpoint: (id: Long) -> V?) =
        Route { request: Request, _: Response ->
            request.params(ID_ENDPOINT_PARAM).toLong().let { id ->
                executeEndpoint(id).let { domain ->
                    if (domain != null) jsonUtil.write(domain)
                    else "$gettingDomainName with ID $id wasn't found"
                }
            }
        }

    protected fun performGetAllViewsEndpointLogic(executeEndpoint: () -> List<V>) =
        Route { _: Request, response: Response ->
            executeEndpoint().let { domain ->
                response.performJsonResponse()
                jsonUtil.write(domain)
            }
        }

    protected fun performUpsertEndpointLogic(dtoClass: Class<DTO>, executeEndpoint: (dto: DTO) -> V) =
        Route { request: Request, response: Response ->
            jsonUtil.read(request.body(), dtoClass).let(executeEndpoint).let { view ->
                response.performJsonResponse()
                jsonUtil.write(view)
            }
        }

    protected fun performDeleteDomainEndpointLogic(deletingDomainName: String, executeEndpoint: (id: Long) -> Unit) =
        Route { request: Request, response: Response ->
            request.params(ID_ENDPOINT_PARAM).toLong().let { id ->
                executeEndpoint(id)
                response.performJsonResponse()
                "$deletingDomainName with ID $id was deleted"
            }
        }

    private fun performPageNotFoundEndpointLogic() = Route { _: Request?, response: Response ->
        response.status(HttpStatus.NOT_FOUND_404)
        jsonUtil.write(ExceptionView(HttpStatus.NOT_FOUND_404, "Page not found"))
    }

    private fun Response.performJsonResponse(status: Int = HttpStatus.OK_200) = this.apply {
        status(status)
        type(MediaType.JSON_UTF_8.type())
    }
}
