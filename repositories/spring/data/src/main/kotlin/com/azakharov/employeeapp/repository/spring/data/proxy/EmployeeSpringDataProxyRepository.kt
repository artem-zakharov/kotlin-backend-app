package com.azakharov.employeeapp.repository.spring.data.proxy

import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import java.util.Optional
import javax.inject.Inject

class EmployeeSpringDataProxyRepository @Inject constructor(
    private val findById: (Long) -> Optional<EmployeeEntity>,
    private val findAllEmployees: () -> List<EmployeeEntity>,
    private val saveEmployee: (EmployeeEntity) -> EmployeeEntity,
    private val deleteById: (Long) -> Unit
) : EmployeeRepository {

    override fun find(id: Long): EmployeeEntity? = findById(id).orElse(null)

    override fun findAll(): List<EmployeeEntity> = findAllEmployees()

    override fun save(entity: EmployeeEntity): EmployeeEntity = saveEmployee(entity)

    override fun update(entity: EmployeeEntity): EmployeeEntity = saveEmployee(entity)

    override fun delete(id: Long) = deleteById(id)
}
