package com.sbg.msgboard.application.rest;

import com.sbg.msgboard.domain.message.dto.ImmutableMessageCreationDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageDTO;
import com.sbg.msgboard.domain.message.dto.ImmutableMessageUpdateDTO;
import com.sbg.msgboard.domain.message.exception.MessageNotFoundException;
import com.sbg.msgboard.domain.message.service.MessageDomainService;
import com.sbg.msgboard.shared.exception.UserAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(path = {"msgboard/api/v1"})
public class MessageController {

  @Resource private MessageDomainService messageDomainService;

  @GetMapping(value = "/message", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ImmutableMessageDTO>> findAll(@RequestParam int page, @RequestParam int size) {
    List<ImmutableMessageDTO> messages = messageDomainService.findAllMessages(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(messages);
  }

  @PreAuthorize("authentication.name.equals((#userId).toString())")
  @PostMapping(
      value = "/user/{userId}/message",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ImmutableMessageDTO> createMessage(
      @PathVariable UUID userId, @Valid @RequestBody ImmutableMessageCreationDTO messageCreationDTO) {
    ImmutableMessageDTO messageDTO = messageDomainService.createMessage(userId, messageCreationDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
  }

  @PreAuthorize("authentication.name.equals((#userId).toString())")
  @PutMapping(
      value = "/user/{userId}/message/{messageId}",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ImmutableMessageDTO> updateMessage(
      @PathVariable UUID userId,
      @PathVariable UUID messageId,
      @Valid @RequestBody ImmutableMessageUpdateDTO messageUpdateDTO)
      throws MessageNotFoundException, UserAuthorizationException {
    ImmutableMessageDTO messageDTO = messageDomainService.updateMessage(userId, messageId, messageUpdateDTO);
    return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
  }

  @PreAuthorize("authentication.name.equals((#userId).toString())")
  @DeleteMapping(value = "/user/{userId}/message/{messageId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteMessage(@PathVariable UUID userId, @PathVariable UUID messageId)
      throws MessageNotFoundException, UserAuthorizationException {
    messageDomainService.deleteMessage(userId, messageId);
  }
}
