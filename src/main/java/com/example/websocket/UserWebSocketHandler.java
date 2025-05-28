package com.example.websocket;

import com.example.protobuf.TickerUpdateOuterClass;
import com.example.ticket.TickerUpdateListener;
import com.example.ticket.TickersUpdateForwarder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Component
public class UserWebSocketHandler implements WebSocketHandler, TickerUpdateListener {

    private final UserSessionStore sessionStore;
    private final TickersUpdateForwarder forwarder;

    public UserWebSocketHandler(UserSessionStore sessionStore,
                                TickersUpdateForwarder forwarder) {
        this.sessionStore = sessionStore;
        this.forwarder = forwarder;
    }

    @PostConstruct
    public void init() {
        forwarder.registerListener(this);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            System.err.println("❌ userId is null in WebSocket session");
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        System.out.println("✅ WebSocket connected as: " + userId);
        sessionStore.add(new UserSession(userId, session));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {}

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        close(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        close(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void close(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        sessionStore.remove(new UserSession(userId, session));
    }

    @Override
    public void onUpdate(TickerUpdateOuterClass.TickerUpdate update) {
        byte[] bytes = update.toByteArray();
        for (UserSession us : sessionStore.all()) {
            try {
                us.getSession().sendMessage(new BinaryMessage(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




