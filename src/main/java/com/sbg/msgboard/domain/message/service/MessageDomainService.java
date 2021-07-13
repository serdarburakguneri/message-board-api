package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.domain.message.dto.MessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.dto.MessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;

import java.util.List;
import java.util.UUID;

public interface MessageDomainService {

  MessageDTO createMessage(UUID userId, MessageCreationDTO messageCreationDTO);

  MessageDTO updateMessage(UUID userId, UUID messageId, MessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException;

  void deleteMessage(UUID userId, UUID messageId)
      throws MessageNotFoundException, UserAuthorizationException;

  List<MessageDTO> findAllMessages(int page, int size);
}
