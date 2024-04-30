package com.closememo.query.infra.converter;

import com.closememo.query.infra.helper.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    return JsonUtils.toJson(attribute);
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    return JsonUtils.fromJson(dbData, new TypeReference<>() {
    });
  }
}
