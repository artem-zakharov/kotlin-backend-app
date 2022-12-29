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
    private val positionService: EmployeePositionService,
    private val positionConverter: EmployeePositionAllSideDomainConverter
) : EmployeePositionController<EmployeePositionDto, EmployeePositionView> {

    override fun get(id: Long): EmployeePositionView? =
        positionService.find(EmployeePositionId(id)).takeIf { it != null }?.let {
            positionConverter.convertToView(it)
        }

    override fun getAll(): List<EmployeePositionView> =
        positionService.findAll().map { positionConverter.convertToView(it) }

    override fun save(dto: EmployeePositionDto): EmployeePositionView = processUpsert(dto) { positionService.save(it) }

    override fun update(dto: EmployeePositionDto): EmployeePositionView =
        processUpsert(dto) { positionService.update(it) }

    override fun delete(id: Long) = positionService.delete(EmployeePositionId(id))

    private fun processUpsert(
        positionDto: EmployeePositionDto,
        upsert: (saving: EmployeePosition) -> EmployeePosition
    ): EmployeePositionView =
        positionConverter.convertToDomain(positionDto).let(upsert).let { positionConverter.convertToView(it) }
}
