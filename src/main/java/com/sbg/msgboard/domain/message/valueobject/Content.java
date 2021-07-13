package com.sbg.msgboard.domain.message.valueobject;

import javax.persistence.Embeddable;

@Embeddable
public class Content {

  public Content(){

  }

  public Content(String text) {
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
