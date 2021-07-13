package com.sbg.msgboard.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class IdentityUtil {

  public static UUID getAuthenticatedUserId() {

    // TODO: null & format check
    return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
  }
}
