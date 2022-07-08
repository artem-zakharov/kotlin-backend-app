package com.azakharov.employeeapp.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty

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
