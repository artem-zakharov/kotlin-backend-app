package com.azakharov.employeeapp.repository.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/entity/EmployeePositionEntity.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/entity/EmployeePositionEntity.java</a>
 */
@Entity
@Table(name = "employee_positions")
data class EmployeePositionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String = ""

) {

    override fun toString(): String {
        return "EmployeePositionEntity(id=$id, name=$name)"
    }
}
