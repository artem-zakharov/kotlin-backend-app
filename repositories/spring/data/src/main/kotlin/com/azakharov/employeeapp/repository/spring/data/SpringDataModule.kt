package com.azakharov.employeeapp.repository.spring.data

import com.azakharov.employeeapp.repository.jpa.EmployeePositionRepository
import com.azakharov.employeeapp.repository.jpa.EmployeeRepository
import com.azakharov.employeeapp.repository.spring.data.proxy.EmployeePositionSpringDataProxyRepository
import com.azakharov.employeeapp.repository.spring.data.proxy.EmployeeSpringDataProxyRepository
import com.google.inject.AbstractModule
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class SpringDataModule : AbstractModule() {

    override fun configure() {
        bindSpringDataRepositories(AnnotationConfigApplicationContext(SpringDataConfig::class.java).beanFactory)
    }

    private fun bindSpringDataRepositories(beanFactory: ListableBeanFactory) {
        val positionSpringDataRepository = beanFactory.getBean(EmployeePositionSpringDataRepository::class.java)
        val employeeSpringDataRepository = beanFactory.getBean(EmployeeSpringDataRepository::class.java)
        super.bind(EmployeePositionRepository::class.java)
             .toInstance(EmployeePositionSpringDataProxyRepository(positionSpringDataRepository))
        super.bind(EmployeeRepository::class.java)
             .toInstance(EmployeeSpringDataProxyRepository(employeeSpringDataRepository))
    }
}