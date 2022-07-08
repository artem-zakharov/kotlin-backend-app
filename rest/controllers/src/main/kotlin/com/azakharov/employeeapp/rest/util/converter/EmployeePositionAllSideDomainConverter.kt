package com.azakharov.employeeapp.rest.util.converter

import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.id.EmployeePositionId
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.view.EmployeePositionView

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeePositionAllSideDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeePositionAllSideDomainConverter.java</a>
 */
class EmployeePositionAllSideDomainConverter : AllSideDomainConverter<EmployeePositionDto, EmployeePosition, EmployeePositionView> {

    private companion object {
        private const val NAME_FIELD_NAME = "name"
    }

    override fun convertToDomain(dto: EmployeePositionDto): EmployeePosition {
        val id = if (dto.id != null) EmployeePositionId(dto.id) else null
        val name = safelyPerformValue(dto.name, NAME_FIELD_NAME)

        return EmployeePosition(id, name)
    }

    override fun convertToView(domain: EmployeePosition): EmployeePositionView {
        val id = if (domain.id != null) domain.id!!.value else error("Position ID can't be null during converting to view")
        return EmployeePositionView(id, domain.name)
    }
}