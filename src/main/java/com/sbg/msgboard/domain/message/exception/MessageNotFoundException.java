package com.sbg.msgboard.domain.message.exception;

public class MessageNotFoundException extends Exception {

  public MessageNotFoundException() {}

  public MessageNotFoundException(String message) {
    super(message);
  }

  public MessageNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
