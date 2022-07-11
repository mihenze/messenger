package com.yakimenko.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yakimenko.messenger.dto.MessageDTO;
import com.yakimenko.messenger.entity.Message;
import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest()
@RunWith(SpringRunner.class)
public class MessageControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private MessageService messageService;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

   @Test
    public void test200Ok() throws Exception {
        this.mockMvc.perform(
                        get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void testGetLastNoMessage() throws Exception {
        Mockito.when(messageService.getMessagesByLimit(2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/message/2/get")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetLastMessage() throws Exception {
        User testUser = User.builder().id(1L).name("ivan").build();

        Mockito.when(messageService.getMessagesByLimit(1)).thenReturn(Arrays.asList(new Message(1L, "message 1", testUser)));

        mockMvc.perform(get("/message/1/get"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("message 1"))
                .andExpect(jsonPath("$[0].name").value(testUser.getName()));

    }

    @Test
    public void testAddMessage() throws Exception{
        User testUser = User.builder().id(1L).name("ivan").build();
        Message messageActual = new Message(1L, "message 1", testUser);
        MessageDTO messageDTO = MessageDTO.from(messageActual);

        Mockito.when(messageService.saveMessage(messageDTO, null)).thenReturn(messageActual);

        String mapper = new ObjectMapper().writeValueAsString(messageDTO);

        mockMvc.perform(post("/message/add").contentType(
                MediaType.APPLICATION_JSON).content(mapper)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testHistoryMessage() throws Exception{
        User testUser = User.builder().id(1L).name("ivan").build();
        Message messageActual = new Message(1L, "history 1", testUser);
        MessageDTO messageDTO = MessageDTO.from(messageActual);

        Mockito.when(messageService.saveMessage(messageDTO, null)).thenReturn(messageActual);

        Mockito.when(messageService.getMessagesByLimit(1)).thenReturn(Arrays.asList(new Message(1L, "message 1", testUser)));

        String mapper = new ObjectMapper().writeValueAsString(messageDTO);

        mockMvc.perform(post("/message/add").contentType(
                                MediaType.APPLICATION_JSON).content(mapper)
                        .accept(MediaType.APPLICATION_JSON))
                //.andExpect(status().isOk()).andReturn();
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("message 1"))
                .andExpect(jsonPath("$[0].name").value(testUser.getName()));
    }


}
