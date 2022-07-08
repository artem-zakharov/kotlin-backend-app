package com.azakharov.employeeapp.rest.util.converter

internal fun <V> safelyPerformValue(value: V?, valueName: String): V =
    value ?: error("$valueName can't be null during converting to domain or view")