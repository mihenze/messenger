package com.yakimenko.messenger.repository;

import com.yakimenko.messenger.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    //получение последних сообщений, заданного количества
    List<Message> findAllByOrderByIdDesc(Pageable pageable);
}
