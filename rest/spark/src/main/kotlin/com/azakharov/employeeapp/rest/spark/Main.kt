package com.azakharov.employeeapp.rest.spark

import com.azakharov.employeeapp.rest.spark.exception.SparkExceptionHandler
import com.azakharov.employeeapp.rest.spark.proxy.EmployeePositionRestSparkProxyController
import com.azakharov.employeeapp.rest.spark.proxy.EmployeeRestSparkProxyController
import com.google.inject.Guice

fun main() {
    Guice.createInjector(RestSparkModule()).run {
        getInstance(EmployeePositionRestSparkProxyController::class.java).initEmployeePositionEndpoints()
        getInstance(EmployeeRestSparkProxyController::class.java).initEmployeeEndpoints()
        getInstance(SparkExceptionHandler::class.java).initExceptionHandlers()
    }
}

private fun EmployeePositionRestSparkProxyController.initEmployeePositionEndpoints() = this.apply {
    getEmployeePosition()
    getAllEmployeePositions()
    save()
    update()
    delete()
}

private fun EmployeeRestSparkProxyController.initEmployeeEndpoints() = this.apply {
    getEmployee()
    getAllEmployees()
    save()
    update()
    delete()
}

private fun SparkExceptionHandler.initExceptionHandlers() = this.apply {
    handleInvalidDomainException()
    handleInvalidTypedIdException()
    handleEmployeeServiceException()
    handleIllegalStateException()
    handleUnexpectedException()
}
