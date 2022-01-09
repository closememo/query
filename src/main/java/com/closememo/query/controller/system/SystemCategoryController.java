package com.closememo.query.controller.system;

import com.closememo.query.controller.system.facade.SystemCategoryFacade;
import com.closememo.query.controller.system.request.RefreshCategoryRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SystemQueryInterface
public class SystemCategoryController {

  private final SystemCategoryFacade systemCategoryFacade;

  public SystemCategoryController(
      SystemCategoryFacade systemCategoryFacade) {
    this.systemCategoryFacade = systemCategoryFacade;
  }

  @PostMapping("/refresh-category")
  public void refreshCategory(@RequestBody RefreshCategoryRequest request) {
    systemCategoryFacade.refreshCategory(request.getAccountId());
  }
}
