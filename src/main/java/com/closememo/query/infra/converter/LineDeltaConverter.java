package com.closememo.query.infra.converter;

import com.closememo.query.controller.client.dto.DifferenceDTO.LineDelta;
import com.closememo.query.infra.helper.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import java.util.List;

public class LineDeltaConverter implements AttributeConverter<List<LineDelta>, String> {

  @Override
  public String convertToDatabaseColumn(List<LineDelta> attribute) {
    return JsonUtils.toJson(attribute);
  }

  @Override
  public List<LineDelta> convertToEntityAttribute(String dbData) {
    return JsonUtils.fromJson(dbData, new TypeReference<>() {
    });
  }
}
