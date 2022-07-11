package com.yakimenko.messenger.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//выдает сообщения клиенту об успешных действиях, либо о конфликтах (409)
@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
