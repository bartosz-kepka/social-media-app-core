package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.ChatItemDTO;
import com.nti.socialmediaappcore.dto.NewChatDTO;
import com.nti.socialmediaappcore.dto.NewMessageDTO;
import com.nti.socialmediaappcore.exception.BadRequestException;
import com.nti.socialmediaappcore.exception.DuplicateException;
import com.nti.socialmediaappcore.exception.NoAccessException;
import com.nti.socialmediaappcore.exception.NotFoundException;
import com.nti.socialmediaappcore.model.Chat;
import com.nti.socialmediaappcore.model.Message;
import com.nti.socialmediaappcore.model.UserIdentity;
import com.nti.socialmediaappcore.repository.ChatRepository;
import com.nti.socialmediaappcore.util.identity.IdsCollector;
import com.nti.socialmediaappcore.util.identity.WithIdentities;
import lombok.RequiredArgsConstructor;
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
    private static final String CANNOT_INVITE_YOURSELF = "Cannot invite yourself to the chat";

    private final ChatRepository chatRepository;

    private final SocketIOService socketIOService;

    private final UserService userService;

    public WithIdentities<Chat> addChat(NewChatDTO newChatDTO) {
        Set<String> membersIds = newChatDTO.getMembersIds();
        String creatorId = userService.getCurrentUserId();

        if (membersIds.contains(creatorId)) {
            throw new BadRequestException(CANNOT_INVITE_YOURSELF);
        }

        membersIds.add(creatorId);

        if (chatRepository.existsByMembersIds(membersIds)) {
            throw new DuplicateException(
                    MessageFormat.format(DUPLICATE_MESSAGE, membersIds.toString())
            );
        }

        Chat chat = new Chat(creatorId, membersIds);
        Chat addedChat = chatRepository.insert(chat);

        Set<UserIdentity> identities = userService.getUserIdentitiesByIds(addedChat.getMembersIds());

        WithIdentities<ChatItemDTO> addedChatItemDTO = new WithIdentities<>(
                new ChatItemDTO(addedChat),
                identities
        );
        this.socketIOService.emitNewChat(addedChatItemDTO);

        return new WithIdentities<>(addedChat, identities);
    }

    public WithIdentities<List<ChatItemDTO>> getChatItems() {
        String currentUserId = userService.getCurrentUserId();
        List<Chat> chats = chatRepository.findAllByMembersIdsContains(currentUserId);
        List<ChatItemDTO> chatItemDTOs = chats
                .stream().map(ChatItemDTO::new)
                .collect(Collectors.toUnmodifiableList());

        Set<String> allMembersIds = IdsCollector.collect(chatItemDTOs);
        Set<UserIdentity> identities = userService.getUserIdentitiesByIds(allMembersIds);
        return new WithIdentities<>(chatItemDTOs, identities);
    }

    public WithIdentities<Chat> getChat(String chatId) {
        Chat chat = getUserChat(chatId);
        Set<UserIdentity> identities = userService.getUserIdentitiesByIds(chat.getMembersIds());
        return new WithIdentities<>(chat, identities);
    }

    public Message addMessage(String chatId, NewMessageDTO newMessageDTO) {
        Chat chat = getUserChat(chatId);
        String currentUserId = userService.getCurrentUserId();
        String messageId = String.valueOf(chat.getMessages().size());
        Message message = new Message(
                messageId,
                currentUserId,
                LocalDateTime.now(Clock.systemUTC()),
                newMessageDTO.getContent()
        );
        chat.getMessages().add(message);

        Message addedMessage = chatRepository.save(chat).getMessages().get(Integer.parseInt(message.getId()));
        socketIOService.emitNewMessage(addedMessage, chatId, chat.getMembersIds());

        return addedMessage;
    }

    private Chat getUserChat(String chatId) {
        String currentUserId = userService.getCurrentUserId();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, chatId)));

        if (!chat.getMembersIds().contains(currentUserId)) {
            throw new NoAccessException(
                    MessageFormat.format(NOT_A_CHAT_MEMBER_MESSAGE, currentUserId, chatId)
            );
        }

        return chat;
    }
}
