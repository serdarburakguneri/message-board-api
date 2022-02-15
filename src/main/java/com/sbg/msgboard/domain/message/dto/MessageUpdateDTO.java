package com.sbg.msgboard.domain.message.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageUpdateDTO.class)
@JsonDeserialize(as = ImmutableMessageUpdateDTO.class)
public abstract class MessageUpdateDTO {
  public abstract String getText();
}
