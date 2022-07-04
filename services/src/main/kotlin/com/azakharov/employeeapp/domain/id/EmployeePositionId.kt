package com.azakharov.employeeapp.domain.id

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeePositionId.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeePositionId.java</a>
 */
data class EmployeePositionId(val value: Long) {

    override fun toString(): String {
        return "EmployeePositionId(id=$value)"
    }
}
