package com.azakharov.employeeapp.repository.spring.data.proxy

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import com.azakharov.employeeapp.repository.spring.data.EmployeePositionSpringDataRepository
import javax.inject.Inject

class EmployeePositionSpringDataProxyRepository @Inject constructor(
    private val employeePositionRepository: EmployeePositionSpringDataRepository
) : EmployeePositionRepository {

    override fun find(id: Long): EmployeePositionEntity? = employeePositionRepository.findById(id).orElse(null)

    override fun findAll(): List<EmployeePositionEntity> = employeePositionRepository.findAll()

    override fun save(entity: EmployeePositionEntity): EmployeePositionEntity = employeePositionRepository.save(entity)

    override fun update(entity: EmployeePositionEntity): EmployeePositionEntity = employeePositionRepository.save(entity)

    override fun delete(id: Long) = employeePositionRepository.deleteById(id)
}