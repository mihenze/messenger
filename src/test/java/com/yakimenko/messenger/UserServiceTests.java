package com.yakimenko.messenger;

import com.yakimenko.messenger.entity.Message;
import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.payload.request.SigninRequest;
import com.yakimenko.messenger.payload.request.SignupRequest;
import com.yakimenko.messenger.repository.UserRepository;
import com.yakimenko.messenger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    void testCreateUser(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("ivan");
        signupRequest.setPassword("asdfghy");

        Mockito.when(userRepository.save(User.builder().name(signupRequest.getName()).password(signupRequest.getPassword()).build()))
                .thenReturn(User.builder().id(1L).name("ivan").password("asdfghy").build());

        User user = userService.createUser(signupRequest);

        Assert.assertEquals(user.getName(), signupRequest.getName());

    }

}
