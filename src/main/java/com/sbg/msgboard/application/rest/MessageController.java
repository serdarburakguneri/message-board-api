package com.sbg.msgboard.application.rest;

import com.sbg.msgboard.domain.message.dto.MessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.MessageDTO;
import com.sbg.msgboard.domain.message.dto.MessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.service.MessageDomainService;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(path = {"message-board/api/v1"})
public class MessageController {

  @Resource private MessageDomainService messageDomainService;

  @GetMapping(value = "/message", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MessageDTO>> findMessagesOfBoard(
      @RequestParam int page, @RequestParam int size) {
    List<MessageDTO> messages = messageDomainService.findAllMessages(page, size);
    return ResponseEntity.status(HttpStatus.CREATED).body(messages);
  }

  @PostMapping(
      value = "/message",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageDTO> addMessageToBoard(
      @RequestBody MessageCreationDTO messageCreationDTO) {
    MessageDTO messageDTO = messageDomainService.createMessage(messageCreationDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
  }

  @PutMapping(
      value = "/message/{messageId}",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageDTO> updateMessageOfBoard(
      @PathVariable UUID messageId, @RequestBody MessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException {
    MessageDTO messageDTO = messageDomainService.updateMessage(messageId, messageUpdateDTO);
    return ResponseEntity.ok(messageDTO);
  }

  @DeleteMapping(value = "/message/{messageId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteMessageOfBoard(@PathVariable UUID messageId)
      throws MessageNotFoundException, UserAuthorizationException {
    messageDomainService.deleteMessage(messageId);
  }
}
