package com.sbg.msgboard.application.exception;

import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFound() {
    return new ResponseEntity(
        new ErrorResponse("Message could not be found!"), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAuthorizationException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized() {
    return new ResponseEntity(
        new ErrorResponse("User is not authorized to perform this action!"),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied() {
    return new ResponseEntity(new ErrorResponse("Access Denied!"), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleRest() {

    return new ResponseEntity(
        new ErrorResponse("An internal server error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
