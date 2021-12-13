package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.CategoryApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.controller.client.dto.CategoryDTO;
import com.closememo.query.controller.client.facade.CategoryFacade;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@CategoryApiTag
@ClientQueryInterface
public class CategoryController {

  private final CategoryFacade categoryFacade;

  public CategoryController(CategoryFacade categoryFacade) {
    this.categoryFacade = categoryFacade;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/categories")
  public List<CategoryDTO> getCategories(@AuthenticationPrincipal AccountId accountId) {
    return categoryFacade.getCategories(accountId.getId());
  }
}
