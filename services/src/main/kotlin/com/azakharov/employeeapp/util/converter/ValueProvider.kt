package com.azakharov.employeeapp.util.converter

import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity

internal fun provideId(id: Long?, entity: Any): Long =
    id ?: throw BidirectionalDomainConverterException("Id can't be null in $entity, during converting")

internal fun providePositionEntity(position: EmployeePositionEntity?, employee: EmployeeEntity): EmployeePositionEntity =
    position ?: throw BidirectionalDomainConverterException("Position can't be null in $employee, during converting")
