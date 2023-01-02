package com.azakharov.employeeapp.rest.spark.proxy

import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import spark.Spark
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeePositionSparkProxyRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeePositionSparkProxyRestController.java</a>
 */
class EmployeePositionRestSparkProxyController @Inject constructor(
    private val getPosition: (Long) -> EmployeePositionView?,
    private val getAllPositions: () -> List<EmployeePositionView>,
    private val savePosition: (EmployeePositionDto) -> EmployeePositionView,
    private val updatePosition: (EmployeePositionDto) -> EmployeePositionView,
    private val deletePosition: (Long) -> Unit,
    jsonUtil: JsonUtil
) : BaseSparkRestController<EmployeePositionDto, EmployeePositionView>(jsonUtil) {

    private companion object {
        private const val API_VERSION = "/api/v1.0"

        private const val GET_EMPLOYEE_POSITION_ENDPOINT = "$API_VERSION/employee-position/:id"
        private const val GET_ALL_EMPLOYEE_POSITIONS_ENDPOINT = "$API_VERSION/employee-position"
        private const val SAVE_EMPLOYEE_POSITION_ENDPOINT = "$API_VERSION/employee-position/save"
        private const val UPDATE_EMPLOYEE_POSITION_ENDPOINT = "$API_VERSION/employee-position/update"
        private const val DELETE_EMPLOYEE_POSITION_ENDPOINT = "$API_VERSION/employee-position/delete/:id"

        private const val DOMAIN_NAME = "Position"
    }

    fun getEmployeePosition() = Spark.get(
        GET_EMPLOYEE_POSITION_ENDPOINT,
        performGetViewEndpointLogic(DOMAIN_NAME) { getPosition(it) }
    )

    fun getAllEmployeePositions() = Spark.get(
        GET_ALL_EMPLOYEE_POSITIONS_ENDPOINT,
        performGetAllViewsEndpointLogic { getAllPositions() }
    )

    fun save() = Spark.post(
        SAVE_EMPLOYEE_POSITION_ENDPOINT,
        performUpsertEndpointLogic(EmployeePositionDto::class.java) { savePosition(it) }
    )

    fun update() = Spark.put(
        UPDATE_EMPLOYEE_POSITION_ENDPOINT,
        performUpsertEndpointLogic(EmployeePositionDto::class.java) { updatePosition(it) }
    )

    fun delete() = Spark.delete(
        DELETE_EMPLOYEE_POSITION_ENDPOINT,
        performDeleteDomainEndpointLogic(DOMAIN_NAME) { deletePosition(it) }
    )
}
