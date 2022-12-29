package com.azakharov.employeeapp.util.converter

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeeId
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/EmployeeBidirectionalDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/EmployeeBidirectionalDomainConverter.java</a>
 */
class EmployeeBidirectionalDomainConverter @Inject constructor(
    private val positionConverter: EmployeePositionBidirectionalDomainConverter
) : BidirectionalDomainConverter<Employee, EmployeeEntity> {

    override fun convertToDomain(entity: EmployeeEntity) = Employee(
        EmployeeId(provideId(entity.id, entity)),
        entity.firstName,
        entity.surname,
        positionConverter.convertToDomain(providePositionEntity(entity.positionEntity, entity))
    )

    override fun convertToEntity(domain: Employee) = EmployeeEntity(
        domain.id?.value,
        domain.firstName,
        domain.surname,
        positionConverter.convertToEntity(domain.position)
    )
}
