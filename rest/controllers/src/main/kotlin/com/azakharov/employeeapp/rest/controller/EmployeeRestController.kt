package com.azakharov.employeeapp.rest.controller

import com.azakharov.employeeapp.api.EmployeeController
import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeeId
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.util.converter.EmployeeAllSideDomainConverter
import com.azakharov.employeeapp.rest.view.EmployeeView
import com.azakharov.employeeapp.service.employee.EmployeeService
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeeRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeeRestController.java</a>
 */
class EmployeeRestController @Inject constructor(
    private val employeeService: EmployeeService,
    private val employeeConverter: EmployeeAllSideDomainConverter
) : EmployeeController<EmployeeDto, EmployeeView> {

    override fun get(id: Long): EmployeeView? =
        employeeService.find(EmployeeId(id)).takeIf { it != null }?.let {
            employeeConverter.convertToView(it)
        }

    override fun getAll(): List<EmployeeView> = employeeService.findAll().map { employeeConverter.convertToView(it) }

    override fun save(dto: EmployeeDto): EmployeeView = processUpsert(dto) { employeeService.save(it) }

    override fun update(dto: EmployeeDto): EmployeeView = processUpsert(dto) { employeeService.update(it) }

    override fun delete(id: Long) = employeeService.delete(EmployeeId(id))

    private fun processUpsert(employeeDto: EmployeeDto, upsert: (saving: Employee) -> Employee): EmployeeView =
        employeeConverter.convertToDomain(employeeDto).let(upsert).let { employeeConverter.convertToView(it) }
}
