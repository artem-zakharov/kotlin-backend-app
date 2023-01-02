package com.azakharov.employeeapp.rest.spark.util

import com.google.common.net.MediaType
import org.eclipse.jetty.http.HttpStatus
import spark.Response

internal fun Response.performJsonResponse(status: Int = HttpStatus.OK_200) = this.apply {
    status(status)
    type(MediaType.JSON_UTF_8.type())
}
