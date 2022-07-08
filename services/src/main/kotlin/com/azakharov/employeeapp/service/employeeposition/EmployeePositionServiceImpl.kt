package com.azakharov.employeeapp.service.employeeposition

import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.id.EmployeePositionId
import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import com.azakharov.employeeapp.util.converter.EmployeePositionBidirectionalDomainConverter
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employeeposition/EmployeePositionServiceImpl.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employeeposition/EmployeePositionServiceImpl.java</a>
 */
class EmployeePositionServiceImpl @Inject constructor(
    private val positionRepository: EmployeePositionRepository,
    private val positionConverter: EmployeePositionBidirectionalDomainConverter
) : EmployeePositionService {

    override fun find(id: EmployeePositionId): EmployeePosition? {
        val positionEntity = positionRepository.find(id.value)
        return if (positionEntity != null) positionConverter.convertToDomain(positionEntity) else null
    }

    override fun findAll(): List<EmployeePosition> = positionRepository.findAll()
                                                                       .map(positionConverter::convertToDomain)

    override fun save(domain: EmployeePosition): EmployeePosition = processUpsert(positionRepository::save, domain)

    override fun update(domain: EmployeePosition): EmployeePosition = processUpsert(positionRepository::update, domain)

    override fun delete(id: EmployeePositionId) = positionRepository.delete(id.value)

    private fun processUpsert(upsert: (saving: EmployeePositionEntity) -> EmployeePositionEntity,
                              savingPosition: EmployeePosition): EmployeePosition {
        val savingEntity = positionConverter.convertToEntity(savingPosition)
        val savedEntity = upsert(savingEntity)
        return positionConverter.convertToDomain(savedEntity)
    }
}