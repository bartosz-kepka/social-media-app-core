package com.nti.socialmediaappcore.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nti.socialmediaappcore.dto.ChatItemDTO;
import com.nti.socialmediaappcore.dto.socket.SocketNewMessageDTO;
import com.nti.socialmediaappcore.jwt.JwtUtils;
import com.nti.socialmediaappcore.model.Message;
import com.nti.socialmediaappcore.util.identity.WithIdentities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;
import java.util.UUID;

@Service(value = "socketIOService")
public class SocketIOService {
    private SocketIOServer socketIOServer;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public SocketIOService(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @PostConstruct
    private void autoStartup() {
        start();
    }

    @PreDestroy
    private void autoStop() {
        stop();
    }

    private final BiMap<String, UUID> userIdToSessionId = HashBiMap.create();

    public void start() {
        socketIOServer.addConnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            boolean isTokenValid = jwtUtils.validateJwtToken(token);
            if (isTokenValid) {
                String userId = jwtUtils.getIdFromJwtToken(token);
                UUID sessionId = client.getSessionId();
                userIdToSessionId.put(userId, sessionId);
                System.out.println(userId + " connected at " + getIpByClient(client) + " with " +
                        "session " + sessionId);
            } else {
                System.out.println("Unauthorized attempt to connect to socket.io server");
                client.disconnect();
            }
        });

        socketIOServer.addDisconnectListener(client -> {
            UUID sessionId = client.getSessionId();
            String userId = userIdToSessionId.inverse().get(sessionId);
            userIdToSessionId.remove(userId);
            System.out.println(userId + " disconnected");
            client.disconnect();
        });

        socketIOServer.start();
    }

    public void emitNewChat(WithIdentities<ChatItemDTO> chatItemDTO) {
        emitEventToGivenUsers(TOPICS.NEW_CHAT, chatItemDTO, chatItemDTO.getPayload().getMembersIds());
    }

    public void emitNewMessage(Message message, String chatId, Set<String> membersIds) {
        SocketNewMessageDTO socketNewMessageDTO = new SocketNewMessageDTO(chatId, message);
        emitEventToGivenUsers(TOPICS.NEW_MESSAGE, socketNewMessageDTO, membersIds);
    }

    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        return sa.substring(1, sa.indexOf(":"));
    }

    private void broadcastEvent(String topic, Object data) {
        this.socketIOServer.getBroadcastOperations().sendEvent(topic, data);
    }

    private void emitEventToGivenUsers(String topic, Object data, Set<String> usersIds) {
        for (String userId : usersIds) {
            if (userIdToSessionId.containsKey(userId)) {
                this.socketIOServer.getClient(userIdToSessionId.get(userId)).sendEvent(topic, data);
            }
        }
    }
}

interface TOPICS {
    String NEW_CHAT = "NEW_CHAT";
    String NEW_MESSAGE = "NEW_MESSAGE";
}
