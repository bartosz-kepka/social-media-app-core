package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.dto.ChatItemDTO;
import com.nti.socialmediaappcore.dto.NewChatDTO;
import com.nti.socialmediaappcore.dto.NewMessageDTO;
import com.nti.socialmediaappcore.model.Chat;
import com.nti.socialmediaappcore.model.Message;
import com.nti.socialmediaappcore.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Chat addChat(@RequestBody @Valid NewChatDTO newChatDTO) {
        return chatService.addChat(newChatDTO);
    }

    @GetMapping
    public List<ChatItemDTO> getChatItems() {
        return chatService.getChatItems();
    }

    @GetMapping("/{chatId}")
    public Chat getChat(@PathVariable String chatId) {
        return chatService.getChat(chatId);
    }

    @PostMapping("/{chatId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message addMessage(
            @PathVariable String chatId,
            @RequestBody @Valid NewMessageDTO newMessageDTO) {
        return chatService.addMessage(chatId, newMessageDTO);
    }
}
