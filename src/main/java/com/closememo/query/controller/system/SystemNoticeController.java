package com.closememo.query.controller.system;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dto.SystemNoticeDTO;
import com.closememo.query.controller.system.facade.SystemNoticeFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@SystemQueryInterface
public class SystemNoticeController {

  private final SystemNoticeFacade systemNoticeFacade;

  public SystemNoticeController(
      SystemNoticeFacade systemNoticeFacade) {
    this.systemNoticeFacade = systemNoticeFacade;
  }

  @GetMapping("/notices")
  public OffsetPage<SystemNoticeDTO> getNotices(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer limit) {

    return systemNoticeFacade.getNotices(page, limit);
  }

  @GetMapping("/notices/{noticeId}")
  public SystemNoticeDTO getNotice(@PathVariable String noticeId) {
    return systemNoticeFacade.getNotice(noticeId);
  }
}
