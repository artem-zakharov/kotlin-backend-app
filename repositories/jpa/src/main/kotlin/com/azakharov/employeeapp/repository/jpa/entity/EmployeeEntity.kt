package com.azakharov.employeeapp.repository.jpa.entity

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.GenerationType

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/entity/EmployeeEntity.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/entity/EmployeeEntity.java</a>
 */
@Entity
@Table(name = "employees")
data class EmployeeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String? = null,

    @Column(name = "surname", nullable = false)
    val surname: String? = null,

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    val positionEntity: EmployeePositionEntity? = null

) {

    override fun toString(): String {
        return "EmployeeEntity(id=$id, firstName=$firstName, surname=$surname, positionEntity=$positionEntity)"
    }
}
