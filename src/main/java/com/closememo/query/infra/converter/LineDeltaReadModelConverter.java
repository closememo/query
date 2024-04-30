package com.closememo.query.infra.converter;

import com.closememo.query.infra.helper.JsonUtils;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModel.LineDelta;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import java.util.List;

public class LineDeltaReadModelConverter implements AttributeConverter<List<LineDelta>, String> {

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
