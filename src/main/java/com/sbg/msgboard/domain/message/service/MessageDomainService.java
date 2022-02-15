package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;

import java.util.List;
import java.util.UUID;

public interface MessageDomainService {

  ImmutableMessageDTO createMessage(UUID userId, ImmutableMessageCreationDTO messageCreationDTO);

  ImmutableMessageDTO updateMessage(UUID userId, UUID messageId, ImmutableMessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException;

  void deleteMessage(UUID userId, UUID messageId)
      throws MessageNotFoundException, UserAuthorizationException;

  List<ImmutableMessageDTO> findAllMessages(int page, int size);
}
