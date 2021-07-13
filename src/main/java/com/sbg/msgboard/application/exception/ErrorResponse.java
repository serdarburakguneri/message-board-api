package com.sbg.msgboard.application.exception;

public class ErrorResponse {

  public ErrorResponse(String message) {
    setMessage(message);
  }

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
