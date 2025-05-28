package com.example.websocket;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionStore {
    private final Set<UserSession> sessions = ConcurrentHashMap.newKeySet();

    public void add(UserSession session) {
        sessions.add(session);
        System.out.println("SESSION STORE: now have " + sessions.size() + " sessions");
    }
    public void remove(UserSession session) {
        sessions.remove(session);
        System.out.println("SESSION STORE: now have " + sessions.size() + " sessions");
    }
    public Set<UserSession> all() { return sessions; }
}
