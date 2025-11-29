package com.example.chat.app.ChatConfig.Controller;

import com.example.chat.app.ChatConfig.Config.UserSocketHandler;
import com.example.chat.app.ChatConfig.Entity.AppUser;
import com.example.chat.app.ChatConfig.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepo repo;
    private final UserSocketHandler userSocketHandler;

    @GetMapping("/search")
    public List<AppUser> search(@RequestParam String q) {
        return repo.findByUsernameContainingIgnoreCase(q);
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser u) {
        return repo.save(u);
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> req) {
        String id = req.get("id");
        String username = req.get("username");
        String avatarBase64 = req.get("avatarBase64");

        if (id == null) {
            return ResponseEntity.badRequest().body("Missing id");
        }

        return repo.findById(id).map(user -> {

            if (username != null && !username.trim().isEmpty()) {
                user.setUsername(username);
            }

            if (avatarBase64 != null && !avatarBase64.trim().isEmpty()) {
                user.setAvatarBase64(avatarBase64);
            }

            repo.save(user);

            // 🔥 BROADCAST PROFILE UPDATE IN REAL-TIME
            userSocketHandler.broadcastProfileUpdate(user);

            return ResponseEntity.ok("Profile updated");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
    @GetMapping(value = "/{id}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> avatar(@PathVariable String id) {
        return (ResponseEntity<byte[]>) repo.findById(id)
                .map(u -> {
                    String b64 = u.getAvatarBase64();
                    if (b64 == null || b64.isEmpty()) return ResponseEntity.notFound().build();
                    // if client stored base64 with data URI prefix, strip it
                    if (b64.startsWith("data:")) {
                        int comma = b64.indexOf(',');
                        if (comma >= 0) b64 = b64.substring(comma + 1);
                    }
                    byte[] bytes = Base64.getDecoder().decode(b64);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
                })
                .orElse(ResponseEntity.<byte[]>notFound().build());
    }
}

