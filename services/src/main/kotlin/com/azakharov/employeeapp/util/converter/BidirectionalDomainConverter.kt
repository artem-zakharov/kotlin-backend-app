package com.azakharov.employeeapp.util.converter

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/BidirectionalDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/BidirectionalDomainConverter.java</a>
 */
interface BidirectionalDomainConverter<D, E> {

    fun convertToDomain(entity: E): D
    fun convertToEntity(domain: D): E
}