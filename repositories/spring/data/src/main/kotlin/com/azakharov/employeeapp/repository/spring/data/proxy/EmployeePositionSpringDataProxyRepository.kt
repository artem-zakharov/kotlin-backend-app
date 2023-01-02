package com.azakharov.employeeapp.repository.spring.data.proxy

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import java.util.Optional
import javax.inject.Inject

class EmployeePositionSpringDataProxyRepository @Inject constructor(
    private val findById: (Long) -> Optional<EmployeePositionEntity>,
    private val findAllPositions: () -> List<EmployeePositionEntity>,
    private val savePosition: (EmployeePositionEntity) -> EmployeePositionEntity,
    private val deleteById: (Long) -> Unit
) : EmployeePositionRepository {

    override fun find(id: Long): EmployeePositionEntity? = findById(id).orElse(null)

    override fun findAll(): List<EmployeePositionEntity> = findAllPositions()

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity = savePosition(entity)

    override fun update(entity: EmployeePositionEntity): EmployeePositionEntity = savePosition(entity)

    override fun delete(id: Long) = deleteById(id)
}
