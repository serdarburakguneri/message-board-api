package com.sbg.msgboard.domain.message.valueobject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class Sender {

  @Column(name = "sender_id")
  private UUID id;

  public Sender() {}

  public Sender(UUID id) {
    this.setId(id);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
