package com.example.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final UserWebSocketHandler userWebSocketHandler;
    private final WebSocketHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(UserWebSocketHandler userWebSocketHandler,
                           WebSocketHandshakeInterceptor handshakeInterceptor) {
        this.userWebSocketHandler = userWebSocketHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(userWebSocketHandler, "/coins")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
