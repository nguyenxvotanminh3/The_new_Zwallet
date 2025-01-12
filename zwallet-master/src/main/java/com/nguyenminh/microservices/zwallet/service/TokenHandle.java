package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.model.PasswordResetToken;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TokenHandle {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public String createPasswordResetToken(UserModel user) {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = 100000 + secureRandom.nextInt(900000);
        String token = String.valueOf(randomNumber);
//        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                user.getId(),
                LocalDateTime.now().plusMinutes(5)
        );
        passwordResetTokenRepository.save(passwordResetToken);
        emailService.sendEmail(user.getEmailAddress(), token);
        return token;
    }

}
