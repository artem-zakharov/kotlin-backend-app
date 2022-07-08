package com.azakharov.employeeapp.rest.util.converter

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.id.EmployeeId
import com.azakharov.employeeapp.rest.dto.EmployeeDto
import com.azakharov.employeeapp.rest.view.EmployeeView
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeeAllSideDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/EmployeeAllSideDomainConverter.java</a>
 */
class EmployeeAllSideDomainConverter @Inject constructor(
    private val positionConverter: EmployeePositionAllSideDomainConverter
) : AllSideDomainConverter<EmployeeDto, Employee, EmployeeView> {

    private companion object {
        private const val FIRST_NAME_FIELD_NAME = "firstName"
        private const val SURNAME_FIELD_NAME = "surname"
        private const val POSITION_DTO_FIELD_NAME = "positionDto"
    }

    override fun convertToDomain(dto: EmployeeDto): Employee {
        val id = if (dto.id != null) EmployeeId(dto.id) else null

        val firstName = safelyPerformValue(dto.firstName, FIRST_NAME_FIELD_NAME)
        val surname = safelyPerformValue(dto.surname, SURNAME_FIELD_NAME)
        val positionDto = safelyPerformValue(dto.positionDto, POSITION_DTO_FIELD_NAME)

        return Employee(id, firstName, surname, positionConverter.convertToDomain(positionDto))
    }

    override fun convertToView(domain: Employee): EmployeeView {
        val id = if (domain.id != null) domain.id!!.value else error("Employee ID can't be null during converting to view")
        val positionDto = positionConverter.convertToView(domain.position)
        return EmployeeView(id, domain.firstName, domain.surname, positionDto)
    }
}