package com.azakharov.employeeapp.rest.spark

import com.azakharov.employeeapp.rest.spark.exception.SparkExceptionHandler
import com.azakharov.employeeapp.rest.spark.proxy.EmployeePositionRestSparkProxyController
import com.azakharov.employeeapp.rest.spark.proxy.EmployeeRestSparkProxyController
import com.google.inject.Guice

fun main() {
    val injector = Guice.createInjector(RestSparkModule())

    initEmployeePositionEndpoints(injector.getInstance(EmployeePositionRestSparkProxyController::class.java))
    initEmployeeEndpoints(injector.getInstance(EmployeeRestSparkProxyController::class.java))
    initExceptionHandlers(injector.getInstance(SparkExceptionHandler::class.java))
}

private fun initEmployeePositionEndpoints(employeePositionController: EmployeePositionRestSparkProxyController) {
    employeePositionController.getEmployeePosition()
    employeePositionController.getAllEmployeePositions()
    employeePositionController.save()
    employeePositionController.update()
    employeePositionController.delete()
}

private fun initEmployeeEndpoints(employeeController: EmployeeRestSparkProxyController) {
    employeeController.getEmployee()
    employeeController.getAllEmployees()
    employeeController.save()
    employeeController.update()
    employeeController.delete()
}

private fun initExceptionHandlers(handler: SparkExceptionHandler) {
    handler.handleInvalidDomainException()
    handler.handleInvalidTypedIdException()
    handler.handleEmployeeServiceException()
    handler.handleIllegalStateException()
    handler.handleUnexpectedException()
}