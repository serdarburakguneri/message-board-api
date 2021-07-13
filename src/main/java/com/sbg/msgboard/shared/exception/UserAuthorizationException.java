package com.sbg.msgboard.shared.exception;

public class UserAuthorizationException extends Exception {

  public UserAuthorizationException() {}

  public UserAuthorizationException(String message) {
    super(message);
  }

  public UserAuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }
}
