package com.loopers.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return attribute != null ? objectMapper.writeValueAsString(attribute) : null;
        } catch (Exception e) {
            log.error("Map을 JSON으로 변환하는 중 오류 발생", e);
            return "{}";
        }
    }
    
    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {}) : null;
        } catch (Exception e) {
            log.error("JSON을 Map으로 변환하는 중 오류 발생", e);
            return Map.of();
        }
    }
}