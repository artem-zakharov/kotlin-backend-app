package com.azakharov.employeeapp.api

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/api/src/main/java/com/azakharov/employeeapp/api/CrudController.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/api/src/main/java/com/azakharov/employeeapp/api/CrudController.java</a>
 */
interface CrudController<DTO, V> {

    fun get(id: Long): V?
    fun getAll(): List<V>
    fun save(dto: DTO): V
    fun update(dto: DTO): V
    fun delete(id: Long)
}