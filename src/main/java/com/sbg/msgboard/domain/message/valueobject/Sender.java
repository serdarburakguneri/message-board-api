package com.sbg.msgboard.domain.message.valueobject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class Sender {

  public Sender() {}

  public Sender(UUID id) {
    setId(id);
  }

  @Column(name = "sender_id")
  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
