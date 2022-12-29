package com.azakharov.employeeapp.rest.util.converter

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/AllSideDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/util/converter/AllSideDomainConverter.java</a>
 */
interface AllSideDomainConverter<DTO, D, V> {

    fun convertToDomain(dto: DTO): D
    fun convertToView(domain: D): V
}
