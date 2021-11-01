package com.closememo.query.infra.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Converter
public class StringRolesConverter implements AttributeConverter<Set<String>, String> {

  public static final String ROLE_DELIMITER = ":";

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    if (CollectionUtils.isEmpty(attribute)) {
      return StringUtils.EMPTY;
    }
    return attribute.stream().map(String::trim).collect(Collectors.joining(ROLE_DELIMITER));
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new HashSet<>();
    }
    return Arrays.stream(StringUtils.split(dbData, ROLE_DELIMITER))
        .collect(Collectors.toSet());
  }
}
