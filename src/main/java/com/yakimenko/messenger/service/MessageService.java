package com.yakimenko.messenger.service;

import com.yakimenko.messenger.dto.MessageDTO;
import com.yakimenko.messenger.entity.Message;
import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.repository.MessageRepository;
import com.yakimenko.messenger.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class MessageService {
    public static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    //сохраняем сообщение в БД
    public Message saveMessage(MessageDTO messageDTO, Principal principal){
        //Достаем пользователя из БД при помощи объета principal
        User user = getUserByPrincipal(principal);

        Message message = Message.builder()
                .text(messageDTO.getText())
                .user(user)
                .build();

        LOG.info("Saving mesage for User: {}", user.getName());
        return messageRepository.save(message);
    }

    //получаем заданное количество последних сообщений из БД
    public List<Message> getMessagesByLimit(int limit){
        Pageable topTen = PageRequest.of(0, limit);
        List<Message> messages = messageRepository.findAllByOrderByIdDesc(topTen);

        return messages;
    }


    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        User user = userRepository.findUserByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + name));

        return user;
    }

}
