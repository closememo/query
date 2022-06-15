package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.DifferenceDAO;
import com.closememo.query.controller.client.dto.DifferenceDTO;
import com.closememo.query.controller.client.dto.SimpleDifferenceDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DifferenceFacade {

  private final DifferenceDAO differenceDAO;

  public DifferenceFacade(DifferenceDAO differenceDAO) {
    this.differenceDAO = differenceDAO;
  }

  public List<SimpleDifferenceDTO> getDifferencesByDocumentId(String ownerId, String documentId) {
    return differenceDAO.getDifferencesByDocumentId(ownerId, documentId);
  }

  public DifferenceDTO getDifference(String ownerId, String differenceId) {
    return differenceDAO.getDifference(ownerId, differenceId);
  }
}
