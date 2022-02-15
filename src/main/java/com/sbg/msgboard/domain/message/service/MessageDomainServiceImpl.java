package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.mapper.MessageMapper;
import com.sbg.msgboard.domain.message.model.Message;
import com.sbg.msgboard.domain.message.repository.MessageRepository;
import com.sbg.msgboard.domain.message.valueobject.Content;
import com.sbg.msgboard.domain.message.valueobject.Sender;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageDomainServiceImpl implements MessageDomainService {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;

  @Autowired
  public MessageDomainServiceImpl(
      MessageRepository messageRepository, MessageMapper messageMapper) {
    this.messageRepository = messageRepository;
    this.messageMapper = messageMapper;
  }

  @Override
  public ImmutableMessageDTO createMessage(
      UUID userId, ImmutableMessageCreationDTO messageCreationDTO) {
    Message message = new Message(new Content(messageCreationDTO.getText()), new Sender(userId));
    message = messageRepository.save(message);
    return messageMapper.toMessageDTO(message);
  }

  @Override
  public ImmutableMessageDTO updateMessage(
      UUID userId, UUID messageId, ImmutableMessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException {
    Message message = findMessageById(messageId);
    validateMessageOwnership(message, userId);
    message.setContent(new Content(messageUpdateDTO.getText()));
    message = messageRepository.save(message);
    return messageMapper.toMessageDTO(message);
  }

  @Override
  public void deleteMessage(UUID userId, UUID messageId)
      throws MessageNotFoundException, UserAuthorizationException {
    Message message = findMessageById(messageId);
    validateMessageOwnership(message, userId);
    messageRepository.delete(message);
  }

  @Override
  public List<ImmutableMessageDTO> findAllMessages(int page, int size) {
    Pageable pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return messageRepository.findAll(pageRequest).map(messageMapper::toMessageDTO).getContent();
  }

  private Message findMessageById(UUID messageId) throws MessageNotFoundException {
    Optional<Message> message = messageRepository.findById(messageId);
    if (message.isEmpty()) {
      throw new MessageNotFoundException();
    }

    return message.get();
  }

  private void validateMessageOwnership(Message message, UUID modifierId)
      throws UserAuthorizationException {

    if (!message.getSender().getId().equals(modifierId)) {
      throw new UserAuthorizationException();
    }
  }
}
