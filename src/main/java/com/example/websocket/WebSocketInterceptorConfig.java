package com.example.websocket;

import com.example.jwt.JwtDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketInterceptorConfig {

    @Bean
    public WebSocketHandshakeInterceptor handshakeInterceptor(JwtDecoder jwtDecoder) {
        return new WebSocketHandshakeInterceptor(jwtDecoder);
    }
}
