package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.DifferenceDAO;
import com.closememo.query.controller.client.dto.DifferenceDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DifferenceFacade {

  private final DifferenceDAO differenceDAO;

  public DifferenceFacade(DifferenceDAO differenceDAO) {
    this.differenceDAO = differenceDAO;
  }

  public List<DifferenceDTO> getDifferencesByDocumentId(String documentId) {
    return differenceDAO.getDifferencesByDocumentId(documentId);
  }
}
