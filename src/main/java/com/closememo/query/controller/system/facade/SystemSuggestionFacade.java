package com.closememo.query.controller.system.facade;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dao.SystemSuggestionDAO;
import com.closememo.query.controller.system.dto.SystemSuggestionDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SystemSuggestionFacade {

  private final SystemSuggestionDAO systemSuggestionDAO;

  public SystemSuggestionFacade(
      SystemSuggestionDAO systemSuggestionDAO) {
    this.systemSuggestionDAO = systemSuggestionDAO;
  }

  public OffsetPage<SystemSuggestionDTO> getSuggestions(int page, int limit, String status) {
    long total = systemSuggestionDAO.count();
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<SystemSuggestionDTO> notices = systemSuggestionDAO.getSuggestions(offset, limit + 1, status);

    boolean hasNext = notices.size() > limit;
    List<SystemSuggestionDTO> truncated = hasNext ? notices.subList(0, limit) : notices;

    return new OffsetPage<>(truncated, total, page, limit, hasNext);
  }
}
