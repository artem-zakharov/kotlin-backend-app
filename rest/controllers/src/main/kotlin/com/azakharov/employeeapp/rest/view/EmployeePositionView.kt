package com.azakharov.employeeapp.rest.view

import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeePositionView(
    @get:JsonProperty("id") val id: Long?,
    @get:JsonProperty("name") val name: String?
) {

    override fun toString(): String {
        return "EmployeePositionView(id=$id, name='$name')"
    }
}
