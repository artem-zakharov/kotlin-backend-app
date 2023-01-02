package com.azakharov.employeeapp.rest.controller

import com.azakharov.employeeapp.api.EmployeeController
import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeeId
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.view.EmployeeView
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeeRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeeRestController.java</a>
 */
class EmployeeRestController @Inject constructor(
    private val find: (EmployeeId) -> Employee?,
    private val findAll: () -> List<Employee>,
    private val save: (Employee) -> Employee,
    private val update: (Employee) -> Employee,
    private val delete: (EmployeeId) -> Unit,
    private val convertToView: (Employee) -> EmployeeView,
    private val convertToDomain: (EmployeeDto) -> Employee
) : EmployeeController<EmployeeDto, EmployeeView> {

    override fun get(id: Long): EmployeeView? = find(EmployeeId(id)).takeIf { it != null }?.let { convertToView(it) }

    override fun getAll(): List<EmployeeView> = findAll().map { convertToView(it) }

    override fun save(dto: EmployeeDto): EmployeeView = processUpsert(dto) { save(it) }

    override fun update(dto: EmployeeDto): EmployeeView = processUpsert(dto) { update(it) }

    override fun delete(id: Long) = delete(EmployeeId(id))

    private fun processUpsert(employeeDto: EmployeeDto, upsert: (saving: Employee) -> Employee): EmployeeView =
        convertToDomain(employeeDto).let(upsert).run(convertToView)
}
