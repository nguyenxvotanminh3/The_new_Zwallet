package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "password_reset_token")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;
    private String token;
    private String userId; // liên kết với user
    private LocalDateTime expirationTime;
}

