package com.closememo.query.config.security.authentication.account;

import com.closememo.query.config.security.authentication.AbstractAuthenticationProvider;
import com.closememo.query.config.security.authentication.ServiceAuthentication;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountAuthenticationProvider extends AbstractAuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    AccountPreAuthentication accountPreAuthentication = (AccountPreAuthentication) authentication.getPrincipal();
    List<SimpleGrantedAuthority> roles = accountPreAuthentication.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
    return new ServiceAuthentication(accountPreAuthentication.getAccountId(), null, roles);
  }
}
