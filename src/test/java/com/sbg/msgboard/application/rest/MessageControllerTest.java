package com.sbg.msgboard.application.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.service.MessageDomainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sbg.msgboard.TestEntityGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
public class MessageControllerTest {

  @Resource private MockMvc mockMvc;
  @Resource private ObjectMapper objectMapper;

  @MockBean private MessageDomainService messageDomainService;

  @Test
  public void test_find_messages() throws Exception {

    List<ImmutableMessageDTO> messages = new ArrayList<>();
    messages.add(generateMessageDTO(UUID.randomUUID(), "msg1"));
    messages.add(generateMessageDTO(UUID.randomUUID(), "msg2"));
    messages.add(generateMessageDTO(UUID.randomUUID(), "msg3"));

    int page = 0;
    int size = messages.size();

    doReturn(messages).when(messageDomainService).findAllMessages(eq(page), eq(size));

    String endpoint =
        MessageFormat.format("/msgboard/api/v1/message?page={0}&size={1}", page, size);

    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(messages)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    List<ImmutableMessageDTO> messageListResult =
        objectMapper.readValue(response, new TypeReference<List<ImmutableMessageDTO>>() {});

    assertEquals(size, messageListResult.size());
  }

  @Test
  public void test_create_message() throws Exception {

    String text = "message to create";
    UUID userId = UUID.randomUUID();

    ImmutableMessageCreationDTO messageCreationDTO = generateMessageCreationDTO(text);
    ImmutableMessageDTO messageDTO = generateMessageDTO(userId, text);

    doReturn(messageDTO)
        .when(messageDomainService)
        .createMessage(eq(userId), any(ImmutableMessageCreationDTO.class));

    String endpoint = MessageFormat.format("/msgboard/api/v1/user/{0}/message", userId);

    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(messageCreationDTO)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    ImmutableMessageDTO createdMessage =
        objectMapper.readValue(response, ImmutableMessageDTO.class);

    assertEquals(createdMessage.getText(), text);
  }

  @Test
  public void test_create_message_when_user_id_is_not_a_proper_uuid() throws Exception {

    String userIdWithInvalidFormat = "0000-000";

    ImmutableMessageCreationDTO messageCreationDTO = generateMessageCreationDTO("test to create");

    String endpoint =
        MessageFormat.format("/msgboard/api/v1/user/{0}/message", userIdWithInvalidFormat);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(messageCreationDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }

  @Test
  public void test_update_message() throws Exception {

    String text = "message to update";
    UUID userId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    ImmutableMessageUpdateDTO messageUpdateDTO = generateMessageUpdateDTO(text);
    ImmutableMessageDTO messageDTO = generateMessageDTO(userId, text);

    doReturn(messageDTO)
        .when(messageDomainService)
        .updateMessage(eq(userId), eq(messageId), any(ImmutableMessageUpdateDTO.class));

    String endpoint =
        MessageFormat.format("/msgboard/api/v1/user/{0}/message/{1}", userId, messageId);

    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.put(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(messageUpdateDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    ImmutableMessageDTO updatedMessage =
        objectMapper.readValue(response, ImmutableMessageDTO.class);

    assertEquals(updatedMessage.getText(), text);
  }

  @Test
  public void test_update_message_when_user_id_is_not_a_proper_uuid() throws Exception {

    String userIdWithInvalidFormat = "0000-000";
    UUID messageId = UUID.randomUUID();

    ImmutableMessageUpdateDTO messageUpdateDTO = generateMessageUpdateDTO("text to update");

    String endpoint =
        MessageFormat.format(
            "/msgboard/api/v1/user/{0}/message/{1}", userIdWithInvalidFormat, messageId);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(messageUpdateDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }

  @Test
  public void test_update_message_when_message_id_is_not_a_proper_uuid() throws Exception {

    UUID userID = UUID.randomUUID();
    String messageIdWithInvalidFormat = "0000-000";

    ImmutableMessageUpdateDTO messageUpdateDTO = generateMessageUpdateDTO("text to update");

    String endpoint =
        MessageFormat.format(
            "/msgboard/api/v1/user/{0}/message/{1}", userID, messageIdWithInvalidFormat);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(messageUpdateDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }

  @Test
  public void test_delete_message() throws Exception {

    UUID userId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    doNothing().when(messageDomainService).deleteMessage(eq(userId), eq(messageId));

    String endpoint =
        MessageFormat.format("/msgboard/api/v1/user/{0}/message/{1}", userId, messageId);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(endpoint))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  public void test_delete_message_when_user_id_is_not_a_proper_uuid() throws Exception {

    String userIdWithInvalidFormat = "0000-000";
    UUID messageId = UUID.randomUUID();

    String endpoint =
        MessageFormat.format(
            "/msgboard/api/v1/user/{0}/message/{1}", userIdWithInvalidFormat, messageId);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(endpoint))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }

  @Test
  public void test_delete_message_when_message_id_is_not_a_proper_uuid() throws Exception {

    UUID userId = UUID.randomUUID();
    String messageIdWithInvalidFormat = "0000-000";

    String endpoint =
        MessageFormat.format(
            "/msgboard/api/v1/user/{0}/message/{1}", userId, messageIdWithInvalidFormat);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(endpoint))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }
}
