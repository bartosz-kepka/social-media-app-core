package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.ChatItemDTO;
import com.nti.socialmediaappcore.dto.NewChatDTO;
import com.nti.socialmediaappcore.dto.NewMessageDTO;
import com.nti.socialmediaappcore.exception.DuplicateException;
import com.nti.socialmediaappcore.exception.NotFoundException;
import com.nti.socialmediaappcore.model.Chat;
import com.nti.socialmediaappcore.model.Message;
import com.nti.socialmediaappcore.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private static final String DUPLICATE_MESSAGE = "Chat with members with ids {0} already exists";
    private static final String NOT_FOUND_MESSAGE = "Chat with id \"{0}\" doesn't exist";
    private static final String NOT_A_CHAT_MEMBER_MESSAGE = "User with id \"{0}\" is not a member of chat with id \"{1}\"";

    private final ChatRepository chatRepository;

    private final SocketIOService socketIOService;

    private final ModelMapper modelMapper;

    public Chat addChat(NewChatDTO newChatDTO) {
        Set<String> membersIds = newChatDTO.getMembersIds();

        if (chatRepository.existsByMembersIds(membersIds)) {
            throw new DuplicateException(MessageFormat.format(
                    DUPLICATE_MESSAGE,
                    membersIds.toString()
            ));
        }

        Chat chat = modelMapper.map(newChatDTO, Chat.class);
        Chat addedChat = chatRepository.insert(chat);

        ChatItemDTO addedChatItem = modelMapper.map(addedChat, ChatItemDTO.class);
        this.socketIOService.emitNewChat(addedChatItem);

        return addedChat;
    }

    public List<ChatItemDTO> getChatItems() {
        // TODO restrict to chats that authenticated user is a member of only
        List<Chat> chats = chatRepository.findAll();

        return chats
                .stream()
                .map(chat -> modelMapper.map(chat, ChatItemDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }

    public Chat getChat(String chatId) {
        // TODO restrict to chats that authenticated user is a member of only
        return chatRepository.findById(chatId).orElseThrow(() ->
                new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, chatId))
        );
    }

    public Message addMessage(String chatId, NewMessageDTO newMessageDTO) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() ->
                new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, chatId))
        );

        // TODO get senderId from security context when is available and remove senderId from NewMessageDTO class
        String senderId = newMessageDTO.getSenderId();

        if (!chat.getMembersIds().contains(senderId)) {
            throw new DuplicateException(MessageFormat.format(
                    NOT_A_CHAT_MEMBER_MESSAGE,
                    senderId,
                    chatId
            ));
        }

        Message message = modelMapper.map(newMessageDTO, Message.class);
        message.setId(String.valueOf(chat.getMessages().size()));
        message.setSenderId(senderId);
        message.setDate(LocalDateTime.now(Clock.systemUTC()));
        chat.getMessages().add(message);

        Message addedMessage = chatRepository.save(chat).getMessages().get(Integer.parseInt(message.getId()));

        socketIOService.emitNewMessage(chatId, addedMessage);

        return addedMessage;
    }
}
