package com.loopers.support

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonMapConverter : AttributeConverter<Map<String, Any>, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: Map<String, Any>?): String? {
        return if (attribute == null) null else objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Map<String, Any>? {
        return if (dbData == null) null else {
            objectMapper.readValue(dbData, object : TypeReference<Map<String, Any>>() {})
        }
    }
}
