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
        beanFactory.getBean(EmployeePositionSpringDataRepository::class.java).apply {
            EmployeePositionSpringDataProxyRepository(
                findById = ::findById,
                findAllPositions = ::findAll,
                savePosition = ::save,
                deleteById = ::deleteById
            ).apply {
                super.bind(EmployeePositionRepository::class.java).toInstance(this)
            }
        }
        beanFactory.getBean(EmployeeSpringDataRepository::class.java).apply {
            EmployeeSpringDataProxyRepository(
                findById = ::findById,
                findAllEmployees = ::findAll,
                saveEmployee = ::save,
                deleteById = ::deleteById
            ).apply {
                super.bind(EmployeeRepository::class.java).toInstance(this)
            }
        }
    }
}
