package com.nti.socialmediaappcore.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.nti.socialmediaappcore.dto.ChatItemDTO;
import com.nti.socialmediaappcore.dto.socket.SocketNewMessageDTO;
import com.nti.socialmediaappcore.model.Message;
import com.nti.socialmediaappcore.util.identity.WithIdentities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service(value = "socketIOService")
public class SocketIOService {
    private SocketIOServer socketIOServer;

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

    public void start() {
        socketIOServer.addConnectListener(client ->
                System.out.println(getIpByClient(client) + " connected")
        );

        socketIOServer.addDisconnectListener(client -> {
            System.out.println(getIpByClient(client) + " disconnected");
            client.disconnect();
        });

        socketIOServer.start();
    }

    public void emitNewChat(WithIdentities<ChatItemDTO> chatItemDTO) {
        this.socketIOServer.getBroadcastOperations().sendEvent(TOPICS.NEW_CHAT, chatItemDTO);
    }

    public void emitNewMessage(String chatId, Message message) {
        SocketNewMessageDTO socketNewMessageDTO = new SocketNewMessageDTO(chatId, message);
        this.socketIOServer.getBroadcastOperations().sendEvent(TOPICS.NEW_MESSAGE, socketNewMessageDTO);
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
}

interface TOPICS {
    String NEW_CHAT = "NEW_CHAT";
    String NEW_MESSAGE = "NEW_MESSAGE";
}
