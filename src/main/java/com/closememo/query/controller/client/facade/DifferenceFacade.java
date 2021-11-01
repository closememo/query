package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.DifferenceDAO;
import com.closememo.query.controller.client.dto.DifferenceDTO;
import org.springframework.stereotype.Component;

@Component
public class DifferenceFacade {

  private final DifferenceDAO differenceDAO;

  public DifferenceFacade(DifferenceDAO differenceDAO) {
    this.differenceDAO = differenceDAO;
  }

  public DifferenceDTO getDifference(String differenceId) {
    return differenceDAO.getDifference(differenceId);
  }
}
