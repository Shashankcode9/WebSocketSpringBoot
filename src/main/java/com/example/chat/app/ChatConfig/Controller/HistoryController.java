package com.example.chat.app.ChatConfig.Controller;

import com.example.chat.app.ChatConfig.Entity.ChatMessage;
import com.example.chat.app.ChatConfig.Repo.ChatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryController {
    private final ChatRepo repo;

    @GetMapping("/history")
    public List<ChatMessage> history(@RequestParam String u1, @RequestParam String u2) {
        return repo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(u1, u2, u1, u2);
    }
}
