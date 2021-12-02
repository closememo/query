package com.closememo.query.controller.shared.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OffsetPage<T> {

  private static final OffsetPage<?> EMPTY = new OffsetPage<>(List.of(), 0L, 0, 0, false);

  private List<T> data;
  private long total;
  private int currentPage;
  private int limit;
  private boolean hasNext;

  public OffsetPage(List<T> data, long total, int currentPage, int limit, boolean hasNext) {
    this.data = data;
    this.total = total;
    this.currentPage = currentPage;
    this.limit = limit;
    this.hasNext = hasNext;
  }

  @SuppressWarnings("unchecked")
  public static <T> OffsetPage<T> empty() {
    return (OffsetPage<T>) EMPTY;
  }
}
