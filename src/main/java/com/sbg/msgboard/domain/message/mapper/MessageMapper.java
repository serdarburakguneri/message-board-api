package com.sbg.msgboard.domain.message.mapper;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.model.Message;

public interface MessageMapper {
  ImmutableMessageDTO toMessageDTO(Message message);
}
