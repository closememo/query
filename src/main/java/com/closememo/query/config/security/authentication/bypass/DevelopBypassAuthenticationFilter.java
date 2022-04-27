package com.closememo.query.config.security.authentication.bypass;

import com.closememo.query.config.security.authentication.ServiceAuthentication;
import com.closememo.query.config.security.authentication.account.AccountAuthenticationProvider;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.config.security.authentication.account.AccountPreAuthentication;
import com.closememo.query.controller.system.dao.SystemAccountDAO;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class DevelopBypassAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  public final static String X_BYPASS_ACCOUNT_ID = "X-Bypass-Account-Id";

  public final SystemAccountDAO systemAccountDAO;

  public DevelopBypassAuthenticationFilter(
      AccountAuthenticationProvider accountAuthenticationProvider,
      SystemAccountDAO systemAccountDAO) {
    this.systemAccountDAO = systemAccountDAO;
    super.setCheckForPrincipalChanges(true);
    super.setAuthenticationManager(new ProviderManager(accountAuthenticationProvider));
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    String accountId = request.getHeader(X_BYPASS_ACCOUNT_ID);

    if (StringUtils.isBlank(accountId)) {
      return null;
    }

    return systemAccountDAO.select(accountId)
        .map(account ->
            new AccountPreAuthentication(
                new AccountId(account.getId()),
                account.getRoles().stream()
                    .map(String::valueOf).collect(Collectors.toSet())))
        .orElse(null);
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return StringUtils.EMPTY;
  }

  @Override
  protected boolean principalChanged(HttpServletRequest request,
      Authentication currentAuthentication) {

    if (currentAuthentication instanceof ServiceAuthentication) {
      return false;
    }

    return super.principalChanged(request, currentAuthentication);
  }
}
