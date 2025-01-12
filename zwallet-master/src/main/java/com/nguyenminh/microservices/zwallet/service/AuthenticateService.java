package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.configuration.JwtResponse;
import com.nguyenminh.microservices.zwallet.configuration.JwtUtils;
import com.nguyenminh.microservices.zwallet.dto.LoginRequest;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin UserDetails từ authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Tạo JWT
            String jwt = jwtUtils.generateJwtToken(userDetails);
            String username = userDetails.getUsername();
            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            UserModel userModel =  userRepository.findByUserName(username);
            userRepository.save(userModel);
            return ResponseEntity.ok(new JwtResponse(jwt, username, roles)); // Trả về JWT trong response
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    public boolean authenticateUserForChangingPassword(String userName, String oldPass) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return passwordEncoder.matches(oldPass, userDetails.getPassword());
    }


}
