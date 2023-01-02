package com.azakharov.employeeapp.rest.controller

import com.azakharov.employeeapp.api.EmployeePositionController
import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.EmployeePositionId
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.util.converter.EmployeePositionAllSideDomainConverter
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import com.azakharov.employeeapp.service.employeeposition.EmployeePositionService
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeePositionRestController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/controller/EmployeePositionRestController.java</a>
 */
class EmployeePositionRestController @Inject constructor(
    private val find: (EmployeePositionId) -> EmployeePosition?,
    private val findAll: () -> List<EmployeePosition>,
    private val save: (EmployeePosition) -> EmployeePosition,
    private val update: (EmployeePosition) -> EmployeePosition,
    private val delete: (EmployeePositionId) -> Unit,
    private val convertToView: (EmployeePosition) -> EmployeePositionView,
    private val convertToDomain: (EmployeePositionDto) -> EmployeePosition
) : EmployeePositionController<EmployeePositionDto, EmployeePositionView> {

    override fun get(id: Long): EmployeePositionView? =
        find(EmployeePositionId(id)).takeIf { it != null }?.let { convertToView(it) }

    override fun getAll(): List<EmployeePositionView> = findAll().map { convertToView(it) }

    override fun save(dto: EmployeePositionDto): EmployeePositionView = processUpsert(dto) { save(it) }

    override fun update(dto: EmployeePositionDto): EmployeePositionView = processUpsert(dto) { update(it) }

    override fun delete(id: Long) = delete(EmployeePositionId(id))

    private fun processUpsert(
        positionDto: EmployeePositionDto,
        upsert: (saving: EmployeePosition) -> EmployeePosition
    ): EmployeePositionView = convertToDomain(positionDto).let(upsert).run(convertToView)
}
