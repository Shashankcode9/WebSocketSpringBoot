package com.example.chat.app.ChatConfig.Config;

import com.example.chat.app.ChatConfig.Entity.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class UserSocketHandler extends TextWebSocketHandler {
    // map userId -> session
    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // expect ?userId=xxx in URI
        String query = session.getUri().getQuery();
        String userId = null;
        if (query != null) {
            for (String part : query.split("&")) {
                String[] kv = part.split("=");
                if (kv.length == 2 && kv[0].equals("userId")) {
                    userId = URLDecoder.decode(kv[1], StandardCharsets.UTF_8.toString());
                    break;
                }
            }
        }
        if (userId != null) {
            sessions.put(userId, session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // remove closed session from map
        sessions.entrySet().removeIf(e -> e.getValue().getId().equals(session.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // relay to your existing message handling logic or ignore here
    }

    // Broadcast profile update to all connected clients
    public void broadcastProfileUpdate(AppUser user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "profileUpdate");
            payload.put("userId", user.getId());
            payload.put("username", user.getUsername());
            // you may want to send avatar as base64 *or* a URL. I'll send a URL to your avatar endpoint:
            payload.put("avatarUrl", "/users/" + user.getId() + "/avatar");

            String json = new ObjectMapper().writeValueAsString(payload);

            TextMessage msg = new TextMessage(json);
            sessions.values().forEach(sess -> {
                try {
                    if (sess.isOpen()) sess.sendMessage(msg);
                } catch (IOException e) {
                    // ignore/log
                }
            });
        } catch (Exception ex) {
            // log
        }
    }

    // optional helper to send to one user only
    public void sendToUser(String userId, String json) {
        WebSocketSession sess = sessions.get(userId);
        if (sess != null && sess.isOpen()) {
            try { sess.sendMessage(new TextMessage(json)); } catch(IOException e) {}
        }
    }
}
