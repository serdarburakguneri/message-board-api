package com.sbg.msgboard.domain.message.model;

import com.sbg.msgboard.domain.message.valueobject.Content;
import com.sbg.msgboard.domain.message.valueobject.Sender;
import com.sbg.msgboard.shared.model.BaseModel;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class Message extends BaseModel {

  public Message() {}

  public Message(Content content, Sender sender) {
    setSender(sender);
    setContent(content);
  }

  @Embedded private Content content;
  @Embedded private Sender sender;

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }
}
