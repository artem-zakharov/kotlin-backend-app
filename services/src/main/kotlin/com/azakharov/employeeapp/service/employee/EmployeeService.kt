package com.azakharov.employeeapp.service.employee

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.id.EmployeeId
import com.azakharov.employeeapp.service.CrudService

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employee/EmployeeService.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employee/EmployeeService.java</a>
 */
interface EmployeeService : CrudService<Employee, EmployeeId>