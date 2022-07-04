package com.azakharov.employeeapp.service.employee

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeePosition
import com.azakharov.employeeapp.domain.id.EmployeeId
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.jpa.entity.EmployeeEntity
import com.azakharov.employeeapp.service.employeeposition.EmployeePositionService
import com.azakharov.employeeapp.service.exception.EmployeePositionNotFoundException
import com.azakharov.employeeapp.util.converter.EmployeeBidirectionalDomainConverter
import javax.inject.Inject

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employee/EmployeeServiceImpl.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/service/employee/EmployeeServiceImpl.java</a>
 */
class EmployeeServiceImpl @Inject constructor(
    private val employeePositionService: EmployeePositionService,
    private val employeeRepository: EmployeeRepository,
    private val employeeConverter: EmployeeBidirectionalDomainConverter
) : EmployeeService {

    override fun find(id: EmployeeId): Employee? {
        val employeeEntity = employeeRepository.find(id.value)
        return if (employeeEntity != null) employeeConverter.convertToDomain(employeeEntity) else null
    }

    override fun findAll(): List<Employee> = employeeRepository.findAll()
                                                               .map(employeeConverter::convertToDomain)

    override fun save(domain: Employee): Employee = processUpsert(employeeRepository::save, domain)

    override fun update(domain: Employee): Employee = processUpsert(employeeRepository::update, domain)

    override fun delete(id: EmployeeId) = employeeRepository.delete(id.value)

    private fun processUpsert(upsert: (saving: EmployeeEntity) -> EmployeeEntity,
                              savingEmployee: Employee): Employee {
        checkPositionOnExisting(savingEmployee.position)

        val savingEntity = employeeConverter.convertToEntity(savingEmployee)
        val savedEntity = upsert(savingEntity)
        return employeeConverter.convertToDomain(savedEntity)
    }

    private fun checkPositionOnExisting(position: EmployeePosition) {
        employeePositionService.find(position.id)
            ?: throw EmployeePositionNotFoundException("Position with ID ${position.id} wasn''t found")
    }
}