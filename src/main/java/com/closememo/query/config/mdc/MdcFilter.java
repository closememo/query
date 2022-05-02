package com.closememo.query.config.mdc;

import com.closememo.query.config.security.authentication.account.AccountId;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MdcFilter extends OncePerRequestFilter {

  private static final String NONE = "NONE";
  private static final String SYSTEM = "SYSTEM";
  private static final String UNKNOWN = "UNKNOWN";
  private static final String USER = "USER";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    MdcUserInfo mdcUserInfo = getMdcUserInfo(authentication);
    MDC.put("userType", mdcUserInfo.type);
    MDC.put("userId", mdcUserInfo.id);
    MDC.put("remoteAddr", request.getRemoteAddr());

    filterChain.doFilter(request, response);
  }

  private MdcUserInfo getMdcUserInfo(Authentication authentication) {
    if (authentication == null) {
      return new MdcUserInfo(UNKNOWN, StringUtils.EMPTY);
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof AccountId) {
      return new MdcUserInfo(USER, ((AccountId) principal).getId());
    }
    if (principal instanceof String) {
      String principalStr = (String) principal;
      if (StringUtils.equals(principalStr, "anonymousUser")) {
        return new MdcUserInfo(NONE, StringUtils.EMPTY);
      }
      if (StringUtils.equals(principalStr, "SYSTEM")) {
        return new MdcUserInfo(SYSTEM, StringUtils.EMPTY);
      }
    }
    return new MdcUserInfo(UNKNOWN, StringUtils.EMPTY);
  }

  private static class MdcUserInfo {
    String type;
    String id;

    public MdcUserInfo(String type, String id) {
      this.type = type;
      this.id = id;
    }
  }
}
