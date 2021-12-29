package com.closememo.query.controller.batch;

import com.closememo.query.infra.batch.BatchService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@BatchQueryInterface
public class BatchController {

  private final BatchService batchService;

  public BatchController(BatchService batchService) {
    this.batchService = batchService;
  }

  @GetMapping("/run")
  public void run() {
    List<String> accountIds = batchService.step1();
    accountIds.forEach(batchService::step2);
  }
}
