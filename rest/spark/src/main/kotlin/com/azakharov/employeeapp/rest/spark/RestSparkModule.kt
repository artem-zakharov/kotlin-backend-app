package com.azakharov.employeeapp.rest.spark

import com.azakharov.employeeapp.ServiceModule
import com.azakharov.employeeapp.api.EmployeeController
import com.azakharov.employeeapp.api.EmployeePositionController
import com.azakharov.employeeapp.rest.controller.EmployeePositionRestController
import com.azakharov.employeeapp.rest.controller.EmployeeRestController
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.spark.exception.SparkExceptionHandler
import com.azakharov.employeeapp.rest.spark.proxy.EmployeePositionRestSparkProxyController
import com.azakharov.employeeapp.rest.spark.proxy.EmployeeRestSparkProxyController
import com.azakharov.employeeapp.rest.util.converter.EmployeeAllSideDomainConverter
import com.azakharov.employeeapp.rest.util.converter.EmployeePositionAllSideDomainConverter
import com.azakharov.employeeapp.rest.util.json.JsonUtil
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import com.azakharov.employeeapp.rest.view.EmployeeView
import com.azakharov.employeeapp.service.employee.EmployeeService
import com.azakharov.employeeapp.service.employee.EmployeeServiceImpl
import com.azakharov.employeeapp.service.employeeposition.EmployeePositionService
import com.azakharov.employeeapp.service.employeeposition.EmployeePositionServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/kotlin-backend-app/blob/master/rest/spark/src/main/kotlin/com/azakharov/employeeapp/rest/spark/RestSparkModule.kt">https://github.com/artemzakharovbelarus/kotlin-backend-app/blob/master/rest/spark/src/main/kotlin/com/azakharov/employeeapp/rest/spark/RestSparkModule.kt</a>
 */
class RestSparkModule : AbstractModule() {

    override fun configure() {
        super.install(ServiceModule())

        bindJsonUtil()
        bindConverters()
        bindControllers()
        bindSparkProxyControllers()

        super.bind(SparkExceptionHandler::class.java)
    }

    private fun bindJsonUtil() {
        super.bind(ObjectMapper::class.java)
        super.bind(JsonUtil::class.java)
    }

    private fun bindConverters() {
        super.bind(EmployeePositionAllSideDomainConverter::class.java)
        super.bind(EmployeeAllSideDomainConverter::class.java)
    }

    private fun bindControllers() {
        bindEmployeePositionController()
        bindEmployeeController()
    }

    private fun bindSparkProxyControllers() {
        super.bind(EmployeeRestSparkProxyController::class.java)
        super.bind(EmployeePositionRestSparkProxyController::class.java)
    }

    private fun bindEmployeePositionController() {
        super.bind(EmployeePositionService::class.java).to(EmployeePositionServiceImpl::class.java)
        super.bind(object: TypeLiteral<EmployeePositionController<EmployeePositionDto, EmployeePositionView>>(){})
             .to(EmployeePositionRestController::class.java)
    }

    private fun bindEmployeeController() {
        super.bind(EmployeeService::class.java).to(EmployeeServiceImpl::class.java)
        super.bind(object: TypeLiteral<EmployeeController<EmployeeDto, EmployeeView>>(){})
             .to(EmployeeRestController::class.java)
    }
}