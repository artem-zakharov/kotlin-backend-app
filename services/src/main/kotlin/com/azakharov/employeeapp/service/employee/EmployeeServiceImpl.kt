package com.azakharov.employeeapp.service.employee

import com.azakharov.employeeapp.domain.Employee
import com.azakharov.employeeapp.domain.EmployeeId
import com.azakharov.employeeapp.domain.EmployeePosition
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

    override fun find(id: EmployeeId): Employee? =
        employeeRepository.find(id.value).let { employeeEntity ->
            takeIf { employeeEntity != null }?.let {
                employeeConverter.convertToDomain(employeeEntity!!)
            }
        }

    override fun findAll(): List<Employee> = employeeRepository.findAll().map(employeeConverter::convertToDomain)

    override fun save(domain: Employee): Employee = processUpsert(domain) { employeeRepository.save(it) }

    override fun update(domain: Employee): Employee = processUpsert(domain) { employeeRepository.update(it) }

    override fun delete(id: EmployeeId) = employeeRepository.delete(id.value)

    private fun processUpsert(
        savingEmployee: Employee,
        upsert: (saving: EmployeeEntity) -> EmployeeEntity
    ): Employee {
        checkPositionOnExisting(savingEmployee.position)

        return employeeConverter.convertToEntity(savingEmployee).let(upsert).run {
            employeeConverter.convertToDomain(this)
        }
    }

    private fun checkPositionOnExisting(position: EmployeePosition) =
        position.id?.let { id ->
            employeePositionService.find(id)
                ?: throw EmployeePositionNotFoundException("Position with ID $id wasn't found")
        } ?: throw EmployeePositionNotFoundException("Position ID can't be null during checking on existing")
}
