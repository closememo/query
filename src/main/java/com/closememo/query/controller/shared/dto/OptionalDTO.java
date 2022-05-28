package com.closememo.query.controller.shared.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionalDTO<T> {

  private static final OptionalDTO<?> EMPTY = new OptionalDTO<>(false, null);

  private boolean exist;
  private T data;

  public OptionalDTO(T data) {
    this(true, data);
  }

  private OptionalDTO(boolean exist, T data) {
    this.exist = exist;
    this.data = data;
  }

  @SuppressWarnings("unchecked")
  public static <T> OptionalDTO<T> empty() {
    return (OptionalDTO<T>) EMPTY;
  }
}
