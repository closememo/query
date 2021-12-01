package com.closememo.query.controller.shared.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OffsetPage<T> {

  private static final OffsetPage<?> EMPTY = new OffsetPage<>(List.of(), null, null, false);

  private List<T> data;
  private Integer currentPage;
  private Integer limit;
  private boolean hasNext;

  public OffsetPage(List<T> data, Integer currentPage, Integer limit, boolean hasNext) {
    this.data = data;
    this.currentPage = currentPage;
    this.limit = limit;
    this.hasNext = hasNext;
  }
}
