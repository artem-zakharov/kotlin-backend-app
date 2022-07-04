package com.azakharov.employeeapp.domain

import com.azakharov.employeeapp.domain.exception.InvalidDomainException

internal fun validateString(value: String, valueName: String, domain: Any) {
    if (value.isBlank()) {
        throw InvalidDomainException("$valueName can''t be null or empty in $domain")
    }
}