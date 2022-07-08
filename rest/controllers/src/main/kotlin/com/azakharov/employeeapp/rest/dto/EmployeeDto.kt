package com.azakharov.employeeapp.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/dto/EmployeeDto.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/dto/EmployeeDto.java</a>
 */
data class EmployeeDto(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("first_name") val firstName: String?,
    @JsonProperty("surname") val surname: String?,
    @JsonProperty("position") val positionDto: EmployeePositionDto?
) {

    override fun toString(): String {
        return "EmployeeDto(id=$id, firstName=$firstName, surname=$surname, positionDto=$positionDto)"
    }
}
