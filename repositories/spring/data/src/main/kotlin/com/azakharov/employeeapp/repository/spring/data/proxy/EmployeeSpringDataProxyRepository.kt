package com.azakharov.employeeapp.repository.spring.data.proxy

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import com.azakharov.employeeapp.repository.spring.data.EmployeeSpringDataRepository
import javax.inject.Inject

class EmployeeSpringDataProxyRepository @Inject constructor(
    private val employeeSpringDataProxyRepository: EmployeeSpringDataRepository
) : EmployeeRepository {

    override fun find(id: Long): EmployeeEntity? = employeeSpringDataProxyRepository.findById(id).orElse(null)

    override fun findAll(): List<EmployeeEntity> = employeeSpringDataProxyRepository.findAll()

    override fun save(entity: EmployeeEntity): EmployeeEntity = employeeSpringDataProxyRepository.save(entity)

    override fun update(entity: EmployeeEntity): EmployeeEntity = employeeSpringDataProxyRepository.save(entity)

    override fun delete(id: Long) = employeeSpringDataProxyRepository.deleteById(id)
}