package com.sbg.msgboard.domain.message.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageDTO.class)
@JsonDeserialize(as = ImmutableMessageDTO.class)
public abstract class MessageDTO {

  public abstract UUID getId();

  public abstract String getText();

  public abstract UUID getSenderId();

  public abstract Instant getCreatedAt();

  public abstract Instant getModifiedAt();

  public abstract int getVersion();
}
