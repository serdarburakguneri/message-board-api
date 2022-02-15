package com.sbg.msgboard.domain.message.valueobject;

import javax.persistence.Embeddable;

@Embeddable
public class Content {
  private String text;

  public Content() {}

  public Content(String text) {
    this.setText(text);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
