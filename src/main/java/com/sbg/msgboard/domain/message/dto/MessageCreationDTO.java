package com.sbg.msgboard.domain.message.dto;

public class MessageCreationDTO {

  public MessageCreationDTO() {}

  public MessageCreationDTO(String text) {
    setText(text);
  }

  private String text;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
