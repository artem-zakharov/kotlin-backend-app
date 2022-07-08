package com.azakharov.employeeapp.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/dto/EmployeePositionDto.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/dto/EmployeePositionDto.java</a>
 */
data class EmployeePositionDto(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name: String?
) {

    override fun toString(): String {
        return "EmployeePositionDto(id=$id, name=$name)"
    }
}
