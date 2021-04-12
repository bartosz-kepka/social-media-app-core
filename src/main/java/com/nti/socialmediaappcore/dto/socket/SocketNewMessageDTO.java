package com.nti.socialmediaappcore.dto.socket;

import com.nti.socialmediaappcore.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketNewMessageDTO {
    private String chatId;

    private String id;

    private String senderId;

    private String date;

    private String content;

    public SocketNewMessageDTO(String chatId, Message message) {
        this.chatId = chatId;
        this.id = message.getId();
        this.senderId = message.getSenderId();
        this.date = message.getDate().toString();
        this.content = message.getContent();
    }
}
