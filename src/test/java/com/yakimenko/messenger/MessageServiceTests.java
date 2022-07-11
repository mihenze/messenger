package com.yakimenko.messenger;

import com.yakimenko.messenger.dto.MessageDTO;
import com.yakimenko.messenger.entity.Message;
import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.repository.MessageRepository;
import com.yakimenko.messenger.repository.UserRepository;
import com.yakimenko.messenger.service.MessageService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTests {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    //@Autowired
    @InjectMocks
    private MessageService messageService;

    @Test
    void testGetMessagesByLimit(){
        User testUser = User.builder().id(1L).name("ivan").build();
        Mockito.when(messageRepository.findAllByOrderByIdDesc(PageRequest.of(0, 2)))
                .thenReturn(Arrays.asList(new Message(1L, "message 1", testUser), new Message(2L, "message 2", testUser)));
        List<Message> messages = messageService.getMessagesByLimit(2);
        Assert.assertTrue(messages.size() == 2);
        Assert.assertEquals(messages.get(0), new Message(1L, "message 1", testUser));
        Assert.assertEquals(messages.get(1), new Message(2L, "message 2", testUser));
    }

    @Test
    void testSaveMessage(){
        User testUser = User.builder().id(1L).name("ivan").build();
        Message messageActual = new Message(1L, "message 1", testUser);
        MessageDTO messageDTO = MessageDTO.from(messageActual);

        Mockito.when(userRepository.findUserByName("ivan"))
                .thenReturn(java.util.Optional.ofNullable(testUser));

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "ivan";
            }
        };

        Mockito.when(messageRepository.save(Message.builder().text("message 1").user(testUser).build())) //Message.builder().id(1L).text("message 1").user(testUser).build()
                .thenReturn(Message.builder().id(1L).text("message 1").user(testUser).build());

        Message message = messageService.saveMessage(messageDTO, principal);
        Assert.assertEquals(message, messageActual);
    }

}
