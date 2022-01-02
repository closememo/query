package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.NoticeApiTag;
import com.closememo.query.controller.client.dto.NoticeDTO;
import com.closememo.query.controller.client.dto.NoticeListElementDTO;
import com.closememo.query.controller.client.facade.NoticeFacade;
import com.closememo.query.controller.shared.dto.OffsetPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@NoticeApiTag
@ClientQueryInterface
public class NoticeController {

  private final NoticeFacade noticeFacade;

  public NoticeController(NoticeFacade noticeFacade) {
    this.noticeFacade = noticeFacade;
  }

  @GetMapping("/notice-list-elements")
  public OffsetPage<NoticeListElementDTO> getNoticeListElements(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer limit) {

    return noticeFacade.getNoticeListElements(page, limit);
  }

  @GetMapping("/notices/{noticeId}")
  public NoticeDTO getNotice(@PathVariable String noticeId) {
    return noticeFacade.getNotice(noticeId);
  }
}
