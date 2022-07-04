package com.azakharov.employeeapp.domain.id

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeeId.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeeId.java</a>
 */
data class EmployeeId(val value: Long) {

    override fun toString(): String {
        return "EmployeeId(id=$value)"
    }
}