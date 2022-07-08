package com.azakharov.employeeapp.rest.spark.proxy

import com.azakharov.employeeapp.api.EmployeePositionController
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import javax.inject.Inject
import spark.Spark

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeePositionSparkProxyRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeePositionSparkProxyRestController.java</a>
 */
class EmployeePositionRestSparkProxyController @Inject constructor(
    private val employeePositionController: EmployeePositionController<EmployeePositionDto, EmployeePositionView>,
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

    fun getEmployeePosition() =
        Spark.get(GET_EMPLOYEE_POSITION_ENDPOINT, performGetViewEndpointLogic(employeePositionController::get, DOMAIN_NAME))

    fun getAllEmployeePositions() =
        Spark.get(GET_ALL_EMPLOYEE_POSITIONS_ENDPOINT, performGetAllViewsEndpointLogic(employeePositionController::getAll))

    fun save() = Spark.post(SAVE_EMPLOYEE_POSITION_ENDPOINT,
                            performUpsertEndpointLogic(employeePositionController::save,  EmployeePositionDto::class.java))

    fun update() = Spark.put(UPDATE_EMPLOYEE_POSITION_ENDPOINT,
                             performUpsertEndpointLogic(employeePositionController::update,  EmployeePositionDto::class.java))

    fun delete() = Spark.delete(DELETE_EMPLOYEE_POSITION_ENDPOINT,
                                performDeleteDomainEndpointLogic(employeePositionController::delete, DOMAIN_NAME))
}