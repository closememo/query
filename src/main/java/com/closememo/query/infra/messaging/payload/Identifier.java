package com.closememo.query.infra.messaging.payload;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Identifier {

  public String id;

  public static String convertToString(Identifier id) {
    return Optional.ofNullable(id).map(Identifier::toString).orElse(null);
  }

  public static List<String> convertToString(List<Identifier> identifiers) {
    if (identifiers == null || identifiers.isEmpty()) {
      return List.of();
    }

    return identifiers.stream().map(Identifier::toString).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return id;
  }
}
