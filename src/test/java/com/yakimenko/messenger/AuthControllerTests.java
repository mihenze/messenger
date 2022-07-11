package com.yakimenko.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.payload.request.SigninRequest;
import com.yakimenko.messenger.payload.request.SignupRequest;
import com.yakimenko.messenger.security.details.UserDetailsImpl;
import com.yakimenko.messenger.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collection;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest()
@RunWith(SpringRunner.class)
public class AuthControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRegisterUser() throws Exception{
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("ivan");
        signupRequest.setPassword("asdfghy");

        Mockito.when(userService.createUser(signupRequest)).thenReturn(User.builder().id(1L).name("ivan").password("asdfghy").build());

        String mapper = new ObjectMapper().writeValueAsString(signupRequest);

        mockMvc.perform(post("/auth/signup")
                        .contentType(
                                MediaType.APPLICATION_JSON).content(mapper)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

}
