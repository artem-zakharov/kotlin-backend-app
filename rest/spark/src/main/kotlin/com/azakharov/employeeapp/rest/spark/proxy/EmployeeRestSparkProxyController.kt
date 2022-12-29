package com.azakharov.employeeapp.rest.spark.proxy

import com.azakharov.employeeapp.api.EmployeeController
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.EmployeeView
import spark.Spark
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeeSparkProxyRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/spark/src/main/java/com/azakharov/employeeapp/rest/spark/proxy/EmployeeSparkProxyRestController.java</a>
 */
class EmployeeRestSparkProxyController @Inject constructor(
    private val employeeController: EmployeeController<EmployeeDto, EmployeeView>,
    jsonUtil: JsonUtil
) : BaseSparkRestController<EmployeeDto, EmployeeView>(jsonUtil) {

    private companion object {
        private const val API_VERSION = "/api/v1.0"

        private const val GET_EMPLOYEE_ENDPOINT = "$API_VERSION/employee/:id"
        private const val GET_ALL_EMPLOYEES_ENDPOINT = "$API_VERSION/employee"
        private const val SAVE_EMPLOYEE_ENDPOINT = "$API_VERSION/employee/save"
        private const val UPDATE_EMPLOYEE_ENDPOINT = "$API_VERSION/employee/update"
        private const val DELETE_EMPLOYEE_ENDPOINT = "$API_VERSION/employee/delete/:id"

        private const val DOMAIN_NAME = "Employee"
    }

    fun getEmployee() = Spark.get(
        GET_EMPLOYEE_ENDPOINT,
        performGetViewEndpointLogic(DOMAIN_NAME) { employeeController.get(it) }
    )

    fun getAllEmployees() = Spark.get(
        GET_ALL_EMPLOYEES_ENDPOINT,
        performGetAllViewsEndpointLogic { employeeController.getAll() }
    )

    fun save() = Spark.post(
        SAVE_EMPLOYEE_ENDPOINT,
        performUpsertEndpointLogic(EmployeeDto::class.java) { employeeController.save(it) }
    )

    fun update() = Spark.put(
        UPDATE_EMPLOYEE_ENDPOINT,
        performUpsertEndpointLogic(EmployeeDto::class.java) { employeeController.update(it) }
    )

    fun delete() = Spark.delete(
        DELETE_EMPLOYEE_ENDPOINT,
        performDeleteDomainEndpointLogic(DOMAIN_NAME) { employeeController.delete(it) }
    )
}
