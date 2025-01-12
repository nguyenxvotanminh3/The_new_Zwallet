package com.nguyenminh.microservices.zwallet.controller;
import com.nguyenminh.microservices.zwallet.configuration.AppConfiguration;
import com.nguyenminh.microservices.zwallet.configuration.JwtResponse;
import com.nguyenminh.microservices.zwallet.configuration.JwtUtils;
import com.nguyenminh.microservices.zwallet.dto.ChangePasswordRequest;
import com.nguyenminh.microservices.zwallet.dto.LoginRequest;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PasswordResetToken;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.PasswordResetTokenRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import com.nguyenminh.microservices.zwallet.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AppConfiguration appConfiguration;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserModelService userModelService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SecurityService securityService;
    private final Mapper mapper;
    private final EncryptPasswordSerivce encryptPasswordSerivce;
    private final AuthenticateService authenticateService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authenticateService.authenticateUser(loginRequest);
    }
    @PostMapping("/login/success")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> loginSuccess() {
        return ResponseEntity.ok("Login successful!");
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Invalid or missing JWT token");
        }
    }
    @PostMapping("/forgot-password")
    @CrossOrigin("*")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        String email = request.get("email");
        ResponseEntity<?> response = securityService.forgotPassword(email);
        return response;
    }

    @PostMapping("/reset-password")
    @CrossOrigin("*")

    public ResponseEntity<?> resetPassword(@RequestParam("t") String t, @RequestBody HashMap<String , String> pass) {
        return securityService.ChangePasswordWithToken(t,pass);
    }



    @PutMapping("/user/change-pass")
    public ResponseEntity<?> changPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(changePasswordRequest.getUserName());
            if (authenticateService.authenticateUserForChangingPassword(changePasswordRequest.getUserName() , changePasswordRequest.getOldPass())) {
                return securityService.ChangePasswordWithoutToken(changePasswordRequest.getUserName(),changePasswordRequest.getNewPass());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password not match");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}