package com.sbg.msgboard.domain.message.repository;

import com.sbg.msgboard.domain.message.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {
  Page<Message> findAll(Pageable pageable);
}
