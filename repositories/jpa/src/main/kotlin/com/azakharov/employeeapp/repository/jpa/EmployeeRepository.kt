package com.azakharov.employeeapp.repository.jpa

import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/EmployeeRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/EmployeeRepository.java</a>
 */
interface EmployeeRepository : CrudRepository<EmployeeEntity, Long>
