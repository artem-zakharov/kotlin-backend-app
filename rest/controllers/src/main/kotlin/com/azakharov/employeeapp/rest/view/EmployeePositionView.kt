package com.azakharov.employeeapp.rest.view

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/view/EmployeePositionView.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/view/EmployeePositionView.java</a>
 */
data class EmployeePositionView(
    @get:JsonProperty("id") val id: Long?,
    @get:JsonProperty("name") val name: String?
) {

    override fun toString(): String {
        return "EmployeePositionView(id=$id, name='$name')"
    }
}
