package com.azakharov.employeeapp.domain

import com.azakharov.employeeapp.domain.id.EmployeePositionId

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/EmployeePosition.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/EmployeePosition.java</a>
 */
data class EmployeePosition(
    val id: EmployeePositionId,
    val name: String = ""
) {

    init {
        validate()
    }

    override fun toString(): String {
        return "EmployeePosition(id=$id, name='$name')"
    }

    private fun validate() {
        validateString(name, "name", this)
    }
}
