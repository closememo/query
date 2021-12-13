package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.CategoryDAO;
import com.closememo.query.controller.client.dto.CategoryDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryFacade {

  private final CategoryDAO categoryDAO;

  public CategoryFacade(CategoryDAO categoryDAO) {
    this.categoryDAO = categoryDAO;
  }

  public List<CategoryDTO> getCategories(String ownerId) {
    return categoryDAO.getCategories(ownerId);
  }
}
