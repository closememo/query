package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.SuggestionDAO;
import com.closememo.query.controller.client.dto.SuggestionDTO;
import com.closememo.query.controller.client.dto.SuggestionListElementDTO;
import com.closememo.query.infra.exception.AccessDeniedException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SuggestionFacade {

  private final SuggestionDAO suggestionDAO;

  public SuggestionFacade(SuggestionDAO suggestionDAO) {
    this.suggestionDAO = suggestionDAO;
  }

  public List<SuggestionListElementDTO> getSuggestionListElements(String writerId) {
    return suggestionDAO.getSuggestionListElements(writerId);
  }

  public SuggestionDTO getSuggestion(String suggestionId, String writerId) {
    SuggestionDTO suggestionDTO = suggestionDAO.getSuggestion(suggestionId);
    checkAuthority(suggestionDTO, writerId);
    return suggestionDTO;
  }

  private void checkAuthority(SuggestionDTO suggestionDTO, String writerId) {
    if (!StringUtils.equals(suggestionDTO.getWriterId(), writerId)) {
      throw new AccessDeniedException();
    }
  }
}
