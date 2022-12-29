package com.azakharov.employeeapp.domain

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/Employee.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/Employee.java</a>
 */
data class Employee(
    val id: EmployeeId?,
    val firstName: String,
    val surname: String,
    val position: EmployeePosition
) {

    init {
        validate()
    }

    override fun toString(): String {
        return "Employee(id=$id, firstName='$firstName', surname='$surname', position=$position)"
    }

    private fun validate() {
        validateString(firstName, "firstName", this)
        validateString(surname, "surname", this)
    }
}

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeeId.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/domain/id/EmployeeId.java</a>
 */
data class EmployeeId(val value: Long) {

    override fun toString(): String {
        return "EmployeeId(id=$value)"
    }
}
