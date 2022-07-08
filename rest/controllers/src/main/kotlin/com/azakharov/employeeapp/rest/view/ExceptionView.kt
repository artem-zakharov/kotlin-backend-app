package com.azakharov.employeeapp.rest.view

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Kotlin Copy of
 * <a href="https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/view/ExceptionView.java">https://github.com/artemzakharovbelarus/employee-module-app/blob/master/rest/controllers/src/main/java/com/azakharov/employeeapp/rest/view/ExceptionView.java</a>
 */
data class ExceptionView(
    @get:JsonProperty("http_code") val httpCode: Int,
    @get:JsonProperty("message") val message: String
) {

    override fun toString(): String {
        return "ExceptionView(httpCode=$httpCode, message='$message')"
    }
}
