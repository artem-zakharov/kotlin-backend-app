package com.azakharov.employeeapp.service

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/CrudService.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/CrudService.java</a>
 */
interface CrudService<D, ID> {

    fun find(id: ID): D?
    fun findAll(): List<D>
    fun save(domain: D): D
    fun update(domain: D): D
    fun delete(id: ID)
}