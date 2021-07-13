package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.domain.message.dto.MessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.dto.MessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.model.Message;
import com.sbg.msgboard.domain.message.repository.MessageRepository;
import com.sbg.msgboard.domain.message.valueobject.Content;
import com.sbg.msgboard.domain.message.valueobject.Sender;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
public class MessageDomainServiceImplTest {

  // testing service implementation
  @Resource private MessageDomainServiceImpl messageDomainService;

  @MockBean private MessageRepository messageRepository;

  @Test
  public void test_create_message() {

    UUID senderId = UUID.randomUUID();
    String text = "testMessage";
    Message expectedMessage = new Message(new Content(text), new Sender(senderId));

    doReturn(expectedMessage).when(messageRepository).save(any(Message.class));

    MessageCreationDTO messageCreationDTO = new MessageCreationDTO(text);
    MessageDTO createdMessageDTO = messageDomainService.createMessage(senderId, messageCreationDTO);

    assertNotNull(createdMessageDTO);
    assertEquals(text, createdMessageDTO.getText());
  }

  @Test
  public void test_update_message_when_message_belongs_to_different_user()
      throws MessageNotFoundException, UserAuthorizationException {

    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();

    Message existingMessage = new Message(new Content("old message"), new Sender(senderId));

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));

    UUID differentSenderId = UUID.randomUUID();

    assertThrows(
        UserAuthorizationException.class,
        () -> {
          String newText = "updated message";
          MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO(newText);
          messageDomainService.updateMessage(differentSenderId, messageId, messageUpdateDTO);
        });
  }

  @Test
  public void test_update_message_when_message_is_not_present() {
    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();

    doReturn(Optional.empty()).when(messageRepository).findById(eq(messageId));

    assertThrows(
        MessageNotFoundException.class,
        () -> {
          messageDomainService.updateMessage(
              messageId, senderId, new MessageUpdateDTO("updated message"));
        });
  }

  @Test
  public void test_update_message() throws MessageNotFoundException, UserAuthorizationException {

    String oldText = "old message";
    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();

    Message existingMessage = new Message(new Content(oldText), new Sender(senderId));

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));

    doReturn(existingMessage).when(messageRepository).save(any(Message.class));

    String newText = "updated message";
    MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO(newText);
    MessageDTO updatedMessageDTO =
        messageDomainService.updateMessage(senderId, messageId, messageUpdateDTO);

    assertNotNull(updatedMessageDTO);
    assertEquals(newText, updatedMessageDTO.getText());
  }

  @Test
  public void test_delete_message_when_message_is_not_present() {
    UUID messageId = UUID.randomUUID();
    UUID modifierUserId = UUID.randomUUID();

    doReturn(Optional.empty()).when(messageRepository).findById(eq(messageId));

    assertThrows(
        MessageNotFoundException.class,
        () -> {
          messageDomainService.deleteMessage(modifierUserId, messageId);
        });
  }

  @Test
  public void test_delete_message_when_message_belongs_to_different_user() {

    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();
    Message existingMessage = new Message(new Content("msg"), new Sender(senderId));

    UUID differentUserId = UUID.randomUUID();

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));

    assertThrows(
        UserAuthorizationException.class,
        () -> {
          messageDomainService.deleteMessage(differentUserId, messageId);
        });
  }

  @Test
  public void test_delete_message() throws MessageNotFoundException, UserAuthorizationException {

    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();
    Message existingMessage = new Message(new Content("msg"), new Sender(senderId));

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));
    doNothing().when(messageRepository).delete(any(Message.class));

    messageDomainService.deleteMessage(senderId, messageId);
  }

  @Test
  public void find_all_messages() {
    List<Message> messageList = new ArrayList<>();
    UUID senderId = UUID.randomUUID();
    messageList.add(new Message(new Content("msg1"), new Sender(senderId)));
    messageList.add(new Message(new Content("msg1"), new Sender(senderId)));
    Page<Message> messagePageResult = new PageImpl<>(messageList);

    int pageNumber = 0;
    int itemCount = messageList.size();

    doReturn(messagePageResult)
        .when(messageRepository)
        .findAll(
            argThat(
                pageable ->
                    pageable.getPageNumber() == pageNumber && pageable.getPageSize() == itemCount));

    List<MessageDTO> messageListResult =
        messageDomainService.findAllMessages(pageNumber, itemCount);

    assertEquals(messageListResult.size(), messageList.size());
  }
}
