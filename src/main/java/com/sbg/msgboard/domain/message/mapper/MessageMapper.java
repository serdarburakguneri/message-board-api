package com.sbg.msgboard.domain.message.mapper;

import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.model.Message;

public interface MessageMapper {
  MessageDTO toMessageDTO(Message message);
}
