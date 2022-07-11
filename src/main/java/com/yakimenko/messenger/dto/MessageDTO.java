package com.yakimenko.messenger.dto;

import com.yakimenko.messenger.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private String text;
    private String name;

    public static MessageDTO from(Message message) {
       return MessageDTO.builder()
                .id(message.getId())
                .text(message.getText())
                .name(message.getUser().getName())
                .build();
    }
}
