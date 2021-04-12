package com.nti.socialmediaappcore.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Value("${socket-io.host}")
    private String host;

    @Value("${socket-io.port}")
    private Integer port;

    @Value("${socket-io.boss-count}")
    private int bossCount;

    @Value("${socket-io.work-count}")
    private int workCount;

    @Value("${socket-io.allow-custom-requests}")
    private boolean allowCustomRequests;

    @Value("${socket-io.upgrade-timeout}")
    private int upgradeTimeout;

    @Value("${socket-io.ping-timeout}")
    private int pingTimeout;

    @Value("${socket-io.ping-interval}")
    private int pingInterval;

    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        return new SocketIOServer(config);
    }
}
