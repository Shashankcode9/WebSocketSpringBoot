package com.example.chat.app.ChatConfig.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String senderId;
    private String receiverId;
    @Lob
    private String message;
    private long timestamp;
    private String messageType;
}
