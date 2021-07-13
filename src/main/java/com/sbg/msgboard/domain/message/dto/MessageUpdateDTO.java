package com.sbg.msgboard.domain.message.dto;

public class MessageUpdateDTO {

  public MessageUpdateDTO(String text, int version) {
    setText(text);
    setVersion(version);
  }

  private String text;
  private int version;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
