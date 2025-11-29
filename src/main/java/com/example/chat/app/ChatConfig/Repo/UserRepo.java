package com.example.chat.app.ChatConfig.Repo;

import com.example.chat.app.ChatConfig.Entity.AppUser;
import com.example.chat.app.ChatConfig.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<AppUser, String> {
    List<AppUser> findByUsernameContainingIgnoreCase(String q);

}

