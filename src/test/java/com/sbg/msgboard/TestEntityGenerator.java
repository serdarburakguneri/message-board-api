package com.sbg.msgboard;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.model.Message;
import com.sbg.msgboard.domain.message.valueobject.Content;
import com.sbg.msgboard.domain.message.valueobject.Sender;

import java.time.Instant;
import java.util.UUID;

public class TestEntityGenerator {

  public static ImmutableMessageCreationDTO generateMessageCreationDTO(String text) {
    return ImmutableMessageCreationDTO.builder().text(text).build();
  }

  public static ImmutableMessageUpdateDTO generateMessageUpdateDTO(String text) {
    return ImmutableMessageUpdateDTO.builder().text(text).build();
  }

  public static ImmutableMessageDTO generateMessageDTO(UUID senderId, String text) {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();
    Instant modifiedAt = Instant.now();
    int version = 0;

    return ImmutableMessageDTO.builder()
        .id(id)
        .senderId(senderId)
        .createdAt(createdAt)
        .modifiedAt(modifiedAt)
        .version(version)
        .text(text)
        .build();
  }

  public static Message generateMessage(UUID senderId, String text) {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();
    Instant modifiedAt = Instant.now();
    int version = 0;
    Message message = new Message(new Content(text), new Sender(senderId));
    message.setId(id);
    message.setCreatedAt(createdAt);
    message.setModifiedAt(modifiedAt);
    message.setVersion(version);
    return message;
  }
}
