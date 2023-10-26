package com.example.demo.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Converter
public class FeaturesConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> features) {
        try {
            return objectMapper.writeValueAsString(features);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String featuresJson) {
        try {
            return objectMapper.readValue(featuresJson, List.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }
}
