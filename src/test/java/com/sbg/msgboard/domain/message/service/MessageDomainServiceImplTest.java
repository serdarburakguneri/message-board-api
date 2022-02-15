package com.sbg.msgboard.domain.message.service;

import com.sbg.msgboard.TestEntityGenerator;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.model.Message;
import com.sbg.msgboard.domain.message.repository.MessageRepository;
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
    Message expectedMessage = TestEntityGenerator.generateMessage(senderId, text);

    doReturn(expectedMessage).when(messageRepository).save(any(Message.class));

    ImmutableMessageCreationDTO messageCreationDTO =
        TestEntityGenerator.generateMessageCreationDTO(text);
    ImmutableMessageDTO createdMessageDTO =
        messageDomainService.createMessage(senderId, messageCreationDTO);

    assertNotNull(createdMessageDTO);
    assertEquals(text, createdMessageDTO.getText());
  }

  @Test
  public void test_update_message_when_message_belongs_to_different_user()
      throws MessageNotFoundException, UserAuthorizationException {

    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();
    String oldText = "old message";

    Message existingMessage = TestEntityGenerator.generateMessage(senderId, oldText);

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));

    UUID differentSenderId = UUID.randomUUID();

    assertThrows(
        UserAuthorizationException.class,
        () -> {
          String newText = "updated message";
          ImmutableMessageUpdateDTO messageUpdateDTO =
              ImmutableMessageUpdateDTO.builder().text(newText).build();
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
              messageId,
              senderId,
              ImmutableMessageUpdateDTO.builder().text("updated message").build());
        });
  }

  @Test
  public void test_update_message() throws MessageNotFoundException, UserAuthorizationException {

    String oldText = "old message";
    UUID messageId = UUID.randomUUID();
    UUID senderId = UUID.randomUUID();

    Message existingMessage = TestEntityGenerator.generateMessage(senderId, oldText);

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));

    doReturn(existingMessage).when(messageRepository).save(any(Message.class));

    String newText = "updated message";
    ImmutableMessageUpdateDTO messageUpdateDTO =
        TestEntityGenerator.generateMessageUpdateDTO(newText);
    ImmutableMessageDTO updatedMessageDTO =
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
    String text = "msg";

    Message existingMessage = TestEntityGenerator.generateMessage(senderId, text);

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
    String text = "msg";

    Message existingMessage = TestEntityGenerator.generateMessage(senderId, text);

    doReturn(Optional.of(existingMessage)).when(messageRepository).findById(eq(messageId));
    doNothing().when(messageRepository).delete(any(Message.class));

    messageDomainService.deleteMessage(senderId, messageId);
  }

  @Test
  public void find_all_messages() {
    List<Message> messageList = new ArrayList<>();
    UUID senderId = UUID.randomUUID();
    messageList.add(TestEntityGenerator.generateMessage(senderId, "msg1"));
    messageList.add(TestEntityGenerator.generateMessage(senderId, "msg2"));
    Page<Message> messagePageResult = new PageImpl<>(messageList);

    int pageNumber = 0;
    int itemCount = messageList.size();

    doReturn(messagePageResult)
        .when(messageRepository)
        .findAll(
            argThat(
                pageable ->
                    pageable.getPageNumber() == pageNumber && pageable.getPageSize() == itemCount));

    List<ImmutableMessageDTO> messageListResult =
        messageDomainService.findAllMessages(pageNumber, itemCount);

    assertEquals(messageListResult.size(), messageList.size());
  }
}
