package com.sbg.msgboard.domain.message.mapper;

import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapperImpl implements MessageMapper {

  @Override
  public MessageDTO toMessageDTO(Message message) {
    MessageDTO messageDTO = new MessageDTO();
    messageDTO.setId(message.getId());
    messageDTO.setCreatedAt(message.getCreatedAt());
    messageDTO.setModifiedAt(message.getModifiedAt());
    messageDTO.setSenderId(message.getSender().getId());
    messageDTO.setText(message.getContent().getText());
    messageDTO.setVersion(message.getVersion());
    return messageDTO;
  }
}
