package com.sbg.msgboard.domain.message.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageCreationDTO.class)
@JsonDeserialize(as = ImmutableMessageCreationDTO.class)
public abstract class MessageCreationDTO {
  public abstract String getText();
}
