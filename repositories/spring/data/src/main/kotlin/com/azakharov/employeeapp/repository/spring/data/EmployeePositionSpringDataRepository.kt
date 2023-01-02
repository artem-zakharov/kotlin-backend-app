package com.azakharov.employeeapp.repository.spring.data

import com.azakharov.employeeapp.repository.jpa.entity.EmployeePositionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeePositionSpringDataRepository : JpaRepository<EmployeePositionEntity, Long>
