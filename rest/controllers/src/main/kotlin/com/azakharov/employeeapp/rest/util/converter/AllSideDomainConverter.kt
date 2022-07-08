package com.azakharov.employeeapp.rest.util.converter

interface AllSideDomainConverter<DTO, D, V> {

    fun convertToDomain(dto: DTO): D
    fun convertToView(domain: D): V
}