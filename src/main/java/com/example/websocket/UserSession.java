package com.example.websocket;

import org.springframework.web.socket.WebSocketSession;

public class UserSession {
    private final String userId;
    private final WebSocketSession session;

    public UserSession(String userId, WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    public String getUserId() { return userId; }
    public WebSocketSession getSession() { return session; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSession)) return false;
        UserSession that = (UserSession) o;
        return userId.equals(that.userId) && session.equals(that.session);
    }
    @Override
    public int hashCode() {
        return userId.hashCode() + session.hashCode();
    }
}

