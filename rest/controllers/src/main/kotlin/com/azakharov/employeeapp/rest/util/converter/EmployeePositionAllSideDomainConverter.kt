package com.azakharov.employeeapp.rest.util.converter

import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.EmployeePositionId
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.view.EmployeePositionView

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeePositionAllSideDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeePositionAllSideDomainConverter.java</a>
 */
class EmployeePositionAllSideDomainConverter :
    AllSideDomainConverter<EmployeePositionDto, EmployeePosition, EmployeePositionView> {

    private companion object {
        private const val NAME_FIELD_NAME = "name"
    }

    override fun convertToDomain(dto: EmployeePositionDto) = EmployeePosition(
        dto.id?.let { EmployeePositionId(it) },
        safelyPerformValue(dto.name, NAME_FIELD_NAME)
    )

    override fun convertToView(domain: EmployeePosition) = EmployeePositionView(
        domain.id?.value ?: error("Position ID can't be null during converting to view"),
        domain.name
    )
}
