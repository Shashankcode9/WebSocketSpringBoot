package com.example.chat.app.ChatConfig.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppUser {
    @Id
    private String id;
    private String username;
    @Lob
    private String avatarBase64;
}
