package com.sbg.msgboard.domain.message.mapper;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapperImpl implements MessageMapper {

  @Override
  public ImmutableMessageDTO toMessageDTO(Message message) {
    ImmutableMessageDTO messageDTO =
        ImmutableMessageDTO.builder()
            .id(message.getId())
            .createdAt(message.getCreatedAt())
            .modifiedAt(message.getModifiedAt())
            .senderId(message.getSender().getId())
            .text(message.getContent().getText())
            .version(message.getVersion())
            .build();
    return messageDTO;
  }
}
