package com.sbg.msgboard.domain.message.dto;

import javax.validation.constraints.NotBlank;

public class MessageCreationDTO {

  public MessageCreationDTO() {}

  public MessageCreationDTO(String text) {
    setText(text);
  }

  @NotBlank(message = "Text is mandatory")
  private String text;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
