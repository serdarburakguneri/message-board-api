package com.sbg.msgboard.application.exception;

import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MessageNotFoundException.class)
  public final ResponseEntity<Object> handleMessageNotFound() {
    return new ResponseEntity("Message could not be found!", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAuthorizationException.class)
  public final ResponseEntity<Object> handleUnauthorized() {
    return new ResponseEntity(
        "User is not authorized to perform this action!", HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handle() {
    return new ResponseEntity(
        "An internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
