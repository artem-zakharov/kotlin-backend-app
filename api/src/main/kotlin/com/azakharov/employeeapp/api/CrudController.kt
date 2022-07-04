package com.azakharov.employeeapp.api

interface CrudController<DTO, V> {

    fun get(id: Long): V
    fun getAll(): List<V>
    fun save(dto: DTO): V
    fun update(dto: DTO): V
    fun delete(dto: DTO)
}