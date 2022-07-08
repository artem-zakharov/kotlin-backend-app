package com.azakharov.employeeapp

import com.azakharov.employeeapp.repository.hibernate.HibernateModule
import com.azakharov.employeeapp.util.converter.EmployeeBidirectionalDomainConverter
import com.azakharov.employeeapp.util.converter.EmployeePositionBidirectionalDomainConverter
import com.google.inject.AbstractModule

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/ServiceModule.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/services/src/main/java/com/azakharov/employeeapp/ServiceModule.java</a>
 */
class ServiceModule : AbstractModule() {

    override fun configure() {
        super.install(HibernateModule())
//        super.install(JdbcModule())

        bindDomainConverters()
    }

    private fun bindDomainConverters() {
        super.bind(EmployeeBidirectionalDomainConverter::class.java)
        super.bind(EmployeePositionBidirectionalDomainConverter::class.java)
    }
}