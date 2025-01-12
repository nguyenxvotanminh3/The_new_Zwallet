package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.configuration.AppConfiguration;
import com.nguyenminh.microservices.zwallet.configuration.JwtResponse;
import com.nguyenminh.microservices.zwallet.dto.LoginRequest;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PasswordResetToken;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.PasswordResetTokenRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final AppConfiguration appConfiguration;
    private final Mapper mapper;
    private final TokenHandle tokenHandle;
    private final EncryptPasswordSerivce encryptPasswordSerivce;

    private final HttpServletRequest request;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    // change password
    public ResponseEntity<?> ChangePasswordWithoutToken(String userName, String pass) {
        UserModel user = userRepository.findByUserName(userName);
        user.setPassword(encryptPasswordSerivce.encryptPassword(pass));
        userRepository.save(user);
        return ResponseEntity.ok("Password successfully change");
    }




    // forgotPassword
    public ResponseEntity<?> forgotPassword(String email) {
        // Fetch all users with the provided email address
        List<UserModel> users = userRepository.findAllByEmailAddress(email);

        // Handle no users found
        if (users.isEmpty()) {
            throw new IllegalStateException("Email not found. Please provide a valid email address.");
        }

        // Handle multiple users with the same email address
        if (users.size() > 1) {
            throw new IllegalStateException("Multiple users found with the same email address. Please contact support.");
        }

        // Retrieve the single user
        UserModel user = users.get(0);

        // Generate a password reset token
        String token = tokenHandle.createPasswordResetToken(user);

        // Return the user response
        return ResponseEntity.ok(mapper.mapToUserResponse(user));
    }

    public ResponseEntity<?> ChangePasswordWithToken(String t,HashMap<String , String> pass) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(t);
        if (passwordResetToken == null || passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now())) {

            return ResponseEntity.badRequest().body("Token is invalid or expired");
        }
        UserModel user = userRepository.findById(passwordResetToken.getUserId()).orElse(null);
        assert user != null;
        UserResponse userResponse = mapper.mapToUserResponse(user);
        user.setPassword(encryptPasswordSerivce.encryptPassword(pass.get("newPass")));
        userRepository.save(user);
        passwordResetTokenRepository.deleteAllById(Collections.singleton(user.getId()));
        return ResponseEntity.ok("Password successfully reset");
    }






}
