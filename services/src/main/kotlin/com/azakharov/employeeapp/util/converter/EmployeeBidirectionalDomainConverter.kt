package com.azakharov.employeeapp.util.converter

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.id.EmployeeId
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/EmployeeBidirectionalDomainConverter.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/util/converter/EmployeeBidirectionalDomainConverter.java</a>
 */
class EmployeeBidirectionalDomainConverter @Inject constructor(
    private val positionConverter: EmployeePositionBidirectionalDomainConverter
) : BidirectionalDomainConverter<Employee, EmployeeEntity> {

    override fun convertToDomain(entity: EmployeeEntity): Employee {
        val idValue = provideNotNullId(entity.id, entity)
        val position = positionConverter.convertToDomain(provideNotNullPositionEntity(entity.positionEntity, entity))
        return Employee(EmployeeId(idValue), entity.firstName, entity.surname, position)
    }

    override fun convertToEntity(domain: Employee): EmployeeEntity {
        val id = if (domain.id != null) domain.id.value else null
        val positionEntity = positionConverter.convertToEntity(domain.position)
        return EmployeeEntity(id, domain.firstName, domain.surname, positionEntity)
    }
}