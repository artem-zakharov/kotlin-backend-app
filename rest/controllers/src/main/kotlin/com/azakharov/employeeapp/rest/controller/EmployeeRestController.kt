package com.azakharov.employeeapp.rest.controller

import com.azakharov.employeeapp.api.EmployeeController
import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.id.EmployeeId
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.util.converter.EmployeeAllSideDomainConverter
import com.azakharov.employeeapp.rest.view.EmployeeView
import com.azakharov.employeeapp.service.employee.EmployeeService
import javax.inject.Inject

class EmployeeRestController @Inject constructor(
    private val employeeService: EmployeeService,
    private val employeeConverter: EmployeeAllSideDomainConverter
) : EmployeeController<EmployeeDto, EmployeeView> {

    override fun get(id: Long): EmployeeView? {
        val employee = employeeService.find(EmployeeId(id))
        return if (employee != null) employeeConverter.convertToView(employee) else null
    }

    override fun getAll(): List<EmployeeView> = employeeService.findAll().map(employeeConverter::convertToView)

    override fun save(dto: EmployeeDto): EmployeeView = processUpsert(employeeService::save, dto)

    override fun update(dto: EmployeeDto): EmployeeView = processUpsert(employeeService::update, dto)

    override fun delete(id: Long) = employeeService.delete(EmployeeId(id))

    private fun processUpsert(upsert: (saving: Employee) -> Employee,
                              employeeDto: EmployeeDto): EmployeeView {
        val employee = employeeConverter.convertToDomain(employeeDto)
        val savedEmployee = upsert(employee)
        return employeeConverter.convertToView(savedEmployee)
    }
}