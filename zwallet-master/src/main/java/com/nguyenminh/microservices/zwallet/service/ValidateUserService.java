package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.exception.UserValidateException;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ValidateUserService {
    private final UserRepository userRepository;

    // check if user exist
    public boolean checkUserName(String name) {
        return userRepository.findByUserName(name) != null;
    }

    public void checkUserIsAcceptToUserApi(String userName){
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!Objects.equals(currentUserName, userName)){
        throw new UserValidateException("You dont have permitsion");
        }
    }

}
