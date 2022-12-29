package com.azakharov.employeeapp.repository.jpa

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/CrudRepository.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/CrudRepository.java</a>
 */
interface CrudRepository<E, ID> {

    fun find(id: ID): E?
    fun findAll(): List<E>
    fun save(entity: E): E
    fun update(entity: E): E
    fun delete(id: ID)
}

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/RepositoryException.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jpa/src/main/java/com/azakharov/employeeapp/repository/jpa/RepositoryException.java</a>
 */
open class RepositoryException(message: String) : RuntimeException(message)
