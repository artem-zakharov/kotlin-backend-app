package com.azakharov.employeeapp.rest.util.converter

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeeId
import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.dto.EmployeePositionDto
import com.azakharov.employeeapp.rest.view.EmployeePositionView
import com.azakharov.employeeapp.rest.view.EmployeeView
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeeAllSideDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeeAllSideDomainConverter.java</a>
 */
class EmployeeAllSideDomainConverter @Inject constructor(
    private val convertPositionToView: (EmployeePosition) -> EmployeePositionView,
    private val convertPositionDtoToDomain: (EmployeePositionDto) -> EmployeePosition
) : AllSideDomainConverter<EmployeeDto, Employee, EmployeeView> {

    private companion object {
        private const val FIRST_NAME_FIELD_NAME = "firstName"
        private const val SURNAME_FIELD_NAME = "surname"
        private const val POSITION_DTO_FIELD_NAME = "positionDto"
    }

    override fun convertToDomain(dto: EmployeeDto) = Employee(
        dto.id?.let { EmployeeId(it) },
        safelyPerformValue(dto.firstName, FIRST_NAME_FIELD_NAME),
        safelyPerformValue(dto.surname, SURNAME_FIELD_NAME),
        convertPositionDtoToDomain(safelyPerformValue(dto.positionDto, POSITION_DTO_FIELD_NAME))
    )

    override fun convertToView(domain: Employee) = EmployeeView(
        domain.id?.value ?: error("Employee ID can't be null during converting to view"),
        domain.firstName,
        domain.surname,
        convertPositionToView(domain.position)
    )
}
