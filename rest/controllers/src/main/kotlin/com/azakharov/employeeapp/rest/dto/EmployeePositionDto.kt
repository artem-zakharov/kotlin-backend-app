package com.azakharov.employeeapp.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeePositionDto(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name: String?
) {

    override fun toString(): String {
        return "EmployeePositionDto(id=$id, name=$name)"
    }
}
