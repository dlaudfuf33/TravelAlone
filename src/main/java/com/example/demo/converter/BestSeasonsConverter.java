package com.example.demo.converter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class BestSeasonsConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = ","; // 리스트 항목을 구분할 구분자

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        // 리스트를 하나의 문자열로 변환 (예: "봄,여름,가을")
        return list != null ? String.join(SEPARATOR, list) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        // 문자열을 리스트로 변환
        return data != null ? Arrays.asList(data.split(SEPARATOR)) : null;
    }
}
