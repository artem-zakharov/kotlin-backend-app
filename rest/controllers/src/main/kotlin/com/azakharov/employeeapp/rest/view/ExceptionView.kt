package com.azakharov.employeeapp.rest.view

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionView(
    @get:JsonProperty("http_code") val httpCode: Int,
    @get:JsonProperty("message") val message: String
) {

    override fun toString(): String {
        return "ExceptionView(httpCode=$httpCode, message='$message')"
    }
}
