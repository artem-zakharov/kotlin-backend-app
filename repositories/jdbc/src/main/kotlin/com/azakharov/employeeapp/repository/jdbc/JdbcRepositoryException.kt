package com.azakharov.employeeapp.repository.jdbc

import com.azakharov.employeeapp.repository.jpa.RepositoryException

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcRepositoryException.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/repositories/jdbc/src/main/java/com/azakharov/employeeapp/repository/jdbc/JdbcRepositoryException.java</a>
 */
class JdbcRepositoryException(message: String) : RepositoryException(message)