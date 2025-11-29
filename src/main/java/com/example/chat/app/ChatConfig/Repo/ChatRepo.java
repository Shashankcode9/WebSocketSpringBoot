package com.example.chat.app.ChatConfig.Repo;

import com.example.chat.app.ChatConfig.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            String s1, String r1, String s2, String r2);
}
