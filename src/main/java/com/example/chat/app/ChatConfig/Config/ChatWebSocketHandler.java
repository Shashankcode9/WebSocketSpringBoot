package com.example.chat.app.ChatConfig.Config;

import com.example.chat.app.ChatConfig.Entity.ChatMessage;
import com.example.chat.app.ChatConfig.Repo.ChatRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatRepo chatRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    // map userId -> session
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // url: /ws?userId=alice
        String q = session.getUri().getQuery();
        String userId = q != null && q.contains("=") ? q.split("=")[1] : UUID.randomUUID().toString();
        session.getAttributes().put("userId", userId);
        sessions.put(userId, session);
        activeUsers.add(userId);
        broadcastActiveUsers();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String,Object> data = mapper.readValue(message.getPayload(), Map.class);
        String type = (String) data.getOrDefault("type", "message");

        if ("message".equals(type)) {
            String sender = (String) data.get("senderId");
            String receiver = (String) data.get("receiverId");
            String msg = (String) data.get("message");
            String messageType = (String) data.getOrDefault("messageType", "text");

            ChatMessage cm = new ChatMessage();
            cm.setSenderId(sender);
            cm.setReceiverId(receiver);
            cm.setMessage(msg);
            cm.setTimestamp(System.currentTimeMillis());
            cm.setMessageType(messageType);
            chatRepo.save(cm);

            Map<String,Object> payload = Map.of(
                    "type", "message",
                    "messageId", cm.getId(),
                    "senderId", sender,
                    "receiverId", receiver,
                    "message", msg,
                    "messageType", messageType,
                    "timestamp", cm.getTimestamp()
            );
            String json = mapper.writeValueAsString(payload);

            // deliver to receiver
            WebSocketSession rc = sessions.get(receiver);
            if (rc != null && rc.isOpen()) rc.sendMessage(new TextMessage(json));

            // echo to sender (so sender UI updates too)
            WebSocketSession sc = sessions.get(sender);
            if (sc != null && sc.isOpen()) sc.sendMessage(new TextMessage(json));

        } else if ("typing".equals(type)) {
            // forward typing events
            String sender = (String) data.get("senderId");
            String receiver = (String) data.get("receiverId");
            Map<String,Object> payload = Map.of(
                    "type","typing",
                    "senderId",sender,
                    "receiverId",receiver,
                    "isTyping", data.get("isTyping")
            );
            String json = mapper.writeValueAsString(payload);
            WebSocketSession rc = sessions.get(receiver);
            if (rc != null && rc.isOpen()) rc.sendMessage(new TextMessage(json));
        } else if ("delivery".equals(type) || "read".equals(type)) {
            // simple forward to sender or store if you want
            String sender = (String) data.get("senderId");
            String receiver = (String) data.get("receiverId");
            String json = mapper.writeValueAsString(data);
            WebSocketSession sc = sessions.get(sender);
            if (sc != null && sc.isOpen()) sc.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);
        activeUsers.remove(userId);
        broadcastActiveUsers();
    }

    private void broadcastActiveUsers() throws Exception {
        Map<String,Object> payload = Map.of("type","activeUsers","users", activeUsers);
        String json = mapper.writeValueAsString(payload);
        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) s.sendMessage(new TextMessage(json));
        }
    }
}