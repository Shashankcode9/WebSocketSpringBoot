//package com.example.chat.app.ChatConfig.Controller;
//
//
//import com.example.chat.app.ChatConfig.Entity.ChatMessage;
//import com.example.chat.app.ChatConfig.Repo.ChatRepo;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Instant;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class ChatRestController {
//
//    private final ChatRepo repo;
//
//    // Fetch chat history between two users
//    @GetMapping("/history")
//    public List<ChatMessage> history(
//            @RequestParam String u1,
//            @RequestParam String u2) {
//        return repo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(u1, u2, u1, u2);
//    }
//}