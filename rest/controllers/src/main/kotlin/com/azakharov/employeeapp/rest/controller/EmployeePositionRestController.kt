package com.azakharov.employeeapp.rest.controller

import com.azakharov.employeeapp.api.EmployeePositionController
import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.id.EmployeePositionId
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.util.converter.EmployeePositionAllSideDomainConverter
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import com.azakharov.employeeapp.service.employeeposition.EmployeePositionService
import javax.inject.Inject

class EmployeePositionRestController @Inject constructor(
    private val positionService: EmployeePositionService,
    private val positionConverter: EmployeePositionAllSideDomainConverter
) : EmployeePositionController<EmployeePositionDto, EmployeePositionView> {

    override fun get(id: Long): EmployeePositionView? {
        val position = positionService.find(EmployeePositionId(id))
        return if (position != null) positionConverter.convertToView(position) else null
    }

    override fun getAll(): List<EmployeePositionView> = positionService.findAll()
                                                                       .map(positionConverter::convertToView)

    override fun save(dto: EmployeePositionDto): EmployeePositionView = processUpsert(positionService::save, dto)

    override fun update(dto: EmployeePositionDto): EmployeePositionView = processUpsert(positionService::update, dto)

    override fun delete(id: Long) = positionService.delete(EmployeePositionId(id))

    private fun processUpsert(upsert: (saving: EmployeePosition) -> EmployeePosition,
                              positionDto: EmployeePositionDto): EmployeePositionView {
        val position = positionConverter.convertToDomain(positionDto)
        val savedPosition = upsert(position)
        return positionConverter.convertToView(savedPosition)
    }
}