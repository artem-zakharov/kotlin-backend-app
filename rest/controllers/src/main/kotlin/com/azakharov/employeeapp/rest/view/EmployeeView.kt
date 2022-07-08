package com.azakharov.employeeapp.rest.view

import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeeView(
    @get:JsonProperty("id") val id: Long,
    @get:JsonProperty("first_name") val firstName: String,
    @get:JsonProperty("surname") val surname: String,
    @get:JsonProperty("position") val positionView: EmployeePositionView,
) {

    override fun toString(): String {
        return "EmployeeView(id=$id, firstName='$firstName', surname='$surname', positionView=$positionView)"
    }
}
