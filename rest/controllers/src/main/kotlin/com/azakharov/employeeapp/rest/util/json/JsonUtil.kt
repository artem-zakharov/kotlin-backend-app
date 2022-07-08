package com.azakharov.employeeapp.rest.util.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import javax.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class JsonUtil @Inject constructor(
    private val objectMapper: ObjectMapper
) {

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(JsonUtil::class.java)
    }

    fun write(dto: Any): String {
        return try {
            objectMapper.writeValueAsString(dto)
        } catch (e: JsonProcessingException) {
            LOGGER.error("Exception during writing object to JSON, message: ${e.message}")
            LOGGER.debug("Exception during writing object to JSON, object: $dto", e)
            throw JsonUtilException("Exception during writing object to JSON, message: ${e.message}")
        }
    }

    fun writeAll(dtos: List<Any>): String {
        return try {
            objectMapper.writeValueAsString(dtos)
        } catch (e: JsonProcessingException) {
            LOGGER.error("Exception during writing objects to JSON, message: ${e.message}")
            LOGGER.debug("Exception during writing objects to JSON, objects: $dtos", e)
            throw JsonUtilException("Exception during writing objects to JSON, message: ${e.message}")
        }
    }

    fun <DTO> read(json: String, dtoClass: Class<DTO>): DTO {
        return try {
            objectMapper.readValue(json, dtoClass);
        } catch (e: IOException) {
            LOGGER.error("Exception during parsing JSON string, message: ${e.message}")
            LOGGER.debug("Exception during parsing JSON string, JSON: $json", e)
            throw JsonUtilException("Exception during parsing JSON InputStream, message: ${e.message}")
        }
    }

    fun <DTO> readAll(json: InputStream, dtoClass: Class<DTO>): List<DTO> {
        val jsonStr = convertToJsonStr(json);
        return try {
            val type = objectMapper.typeFactory.constructCollectionType(List::class.java, dtoClass)
            objectMapper.readValue(jsonStr, type)
        } catch (e: IOException) {
            LOGGER.error("Exception during parsing JSON InputStream, message: ${e.message}")
            LOGGER.debug("Exception during parsing JSON InputStream, JSON: $jsonStr", e)
            throw JsonUtilException("Exception during parsing JSON InputStream, message: ${e.message}")
        }
    }

    private fun convertToJsonStr(stream: InputStream): String? {
        return BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"))
    }
}