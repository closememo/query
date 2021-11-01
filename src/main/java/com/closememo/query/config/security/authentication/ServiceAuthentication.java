package com.closememo.query.config.security.authentication;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ServiceAuthentication extends UsernamePasswordAuthenticationToken {

  public ServiceAuthentication(Object principal, Object details,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, null, authorities);
    super.setDetails(details);
  }
}
