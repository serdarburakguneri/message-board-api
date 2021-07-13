package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.domain.message.dto.MessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.dto.MessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.mapper.MessageMapper;
import com.sbg.msgboard.domain.message.model.Message;
import com.sbg.msgboard.domain.message.repository.MessageRepository;
import com.sbg.msgboard.domain.message.valueobject.Content;
import com.sbg.msgboard.domain.message.valueobject.Sender;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageDomainServiceImpl implements MessageDomainService {

  @Resource private MessageRepository messageRepository;
  @Resource private MessageMapper messageMapper;

  @Value("${security.configuration.enabled}")
  private boolean securityEnabled;

  @Override
  public MessageDTO createMessage(UUID userId, MessageCreationDTO messageCreationDTO) {
    Message message = new Message(new Content(messageCreationDTO.getText()), new Sender(userId));
    messageRepository.save(message);
    return messageMapper.toMessageDTO(message);
  }

  @Override
  public MessageDTO updateMessage(UUID userId, UUID messageId, MessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException {
    Message message = findMessageById(messageId);
    validateMessageOwnership(message, userId);
    message.setContent(new Content(messageUpdateDTO.getText()));
    messageRepository.save(message);
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
  public List<MessageDTO> findAllMessages(int page, int size) {
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
