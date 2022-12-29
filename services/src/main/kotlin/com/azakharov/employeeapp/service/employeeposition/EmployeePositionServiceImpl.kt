package com.azakharov.employeeapp.service.employeeposition

import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.EmployeePositionId
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

    override fun find(id: EmployeePositionId): EmployeePosition? =
        positionRepository.find(id.value).let { positionEntity ->
            takeIf { positionEntity != null }?.let {
                positionConverter.convertToDomain(positionEntity!!)
            }
        }

    override fun findAll(): List<EmployeePosition> = positionRepository.findAll().map(positionConverter::convertToDomain)

    override fun save(domain: EmployeePosition): EmployeePosition = processUpsert(domain) { positionRepository.save(it) }

    override fun update(domain: EmployeePosition): EmployeePosition = processUpsert(domain) { positionRepository.update(it) }

    override fun delete(id: EmployeePositionId) = positionRepository.delete(id.value)

    private fun processUpsert(
        savingPosition: EmployeePosition,
        upsert: (saving: EmployeePositionEntity) -> EmployeePositionEntity
    ): EmployeePosition =
        positionConverter.convertToEntity(savingPosition).let(upsert).run {
            positionConverter.convertToDomain(this)
        }
}
