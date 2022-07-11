package com.yakimenko.messenger.controller;

import com.yakimenko.messenger.dto.MessageDTO;
import com.yakimenko.messenger.entity.Message;
import com.yakimenko.messenger.service.MessageService;
import com.yakimenko.messenger.service.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    private MessageService messageService;

    //создание сообщения
    /**Эндпоинт сохранения сообщения
     * структура изменена, в теле запроса приходит
     * Сообщения клиента-пользователя:
     * {
     *     text:  "текст сообщение"
     * }
     * в загаловке указывается Bearer
     * имя пользователя берется из авторизации
     *
     * так же здесь, в соответствии с заданием
     * реализован эндпоинт выдачи последних сообщений пользователю
     * (хоть я считаю, что уместнее использовать GET запрос, который я привел ниже)
     * Сообщения клиента-пользователя:
     *      * {
     *      *     text:  "history n"
     *      * }
     *      где n - количество последних сообщений, которые нужно передать пользователю
     * */
    @PostMapping("/add")
    public ResponseEntity<Object> addMessage(@Valid @RequestBody MessageDTO messageDTO, BindingResult bindingResult, Principal principal){
        //проверяем на ошибки
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors; //если есть вернем ошибки

        //проверяем сообщение, которое пришло
        // если вида "history n" то получаем N - ое кол-во последних сообщений
        String text = messageDTO.getText();
        String prefix = text.substring(0, 7); //history 10
        if (prefix.equals("history")){
            String [] part = text.split(" ");
            //проверяем есть ли после префикса число, если есть возвращаем запрос, иначе это простое сообщение
            if (part.length == 2 && part[1]!= null && isNumeric(part[1])) { //someString.chars().allMatch( Character::isDigit );
                List<MessageDTO> messageDTOList = messageService.getMessagesByLimit(Integer.parseInt(part[1])).stream()
                        .map(MessageDTO::from)
                        .collect(Collectors.toList());

                return new ResponseEntity<>(messageDTOList, HttpStatus.OK);
            }
        }

        //иначе - сохраняем сообщение
        Message message = messageService.saveMessage(messageDTO, principal);
        MessageDTO createdMessage = MessageDTO.from(message);

        return new ResponseEntity<>(createdMessage, HttpStatus.OK);
        /**выдаем результат, для сохранения, в виде
         * {
         *     "id": "id сообщения",
         *     "text": "текст сообщения",
         *     "name": "имя пользователя, написавшего сообщение"
         * }
         * а для последних N сообщений в виде массива, с той же структурой
         * */
    }

    /**Эндпоинт получения последних сообщений заданного количества
     * структура изменена, тело запроса отсутствует
     * используется GET запрос
     * в заголовке передается параметр количества последних сообщений, которые нужно передать пользователю
     * в загаловке указывается Bearer
     * */
    //получение N - го кол-ва последних сообщений
    @GetMapping("/{limitMessage}/get")
    public ResponseEntity<Object> getLastMessages(@PathVariable("limitMessage") String limitMessage){

        List<MessageDTO> messageDTOList = messageService.getMessagesByLimit(Integer.parseInt(limitMessage)).stream()
                .map(MessageDTO::from)
                .collect(Collectors.toList());

        return new ResponseEntity<>(messageDTOList, HttpStatus.OK);
        /**выдаем результат в виде массива данных:
         * {
         *     "id": "id сообщения",
         *     "text": "текст сообщения",
         *     "name": "имя пользователя, написавшего сообщение"
         * }
         * */
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}
