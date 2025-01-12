package com.nguyenminh.microservices.zwallet.controller;
import com.nguyenminh.microservices.zwallet.dto.TagAndQuotesRequest;
import com.nguyenminh.microservices.zwallet.dto.UserRegistrationDto;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponsePlayShare;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.service.ImageService;
import com.nguyenminh.microservices.zwallet.service.UserModelService;
import com.nguyenminh.microservices.zwallet.service.ValidateUserService;
import com.nguyenminh.microservices.zwallet.service.WalletService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/api/v1")

@PreAuthorize("hasRole('USER')")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserModelService userModelService;
    private final WalletService walletService;
    private final ValidateUserService validateUserService;
    private final ImageService imageService;

//    @GetMapping("/user/all")
//    @ResponseStatus(HttpStatus.OK)
//    public List<UserResponse> getAllUser(){
//        return userModelService.getAllUser();
//    }


//    @GetMapping("/user/{id}")
//    @ResponseStatus(HttpStatus.FOUND)
//    public Optional<UserResponse> getUserById(@PathVariable String id){
//        return userModelService.getUserById(id);
//    }

    @PutMapping("/user/update/{name}")
    public UserResponse updateUserDetail(@PathVariable String name, @RequestBody UserModel userModel){
        return userModelService.updateUserDetail(name,userModel);
    }

    @PutMapping("/user/update-total-amount/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUserTotal(@PathVariable String name, @RequestBody String totalAmount){
        return walletService.updateUserTotal(name,totalAmount);
    }



    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.OK)
    public UserModel createUser(@RequestBody UserRegistrationDto userRegistrationDto){

        return userModelService.createUser(userRegistrationDto);
    }

    @GetMapping("/check-user/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String name) {


        boolean exists = validateUserService.checkUserName(name);

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("available", !exists);
        response.put("message", exists ? "Username is already taken" : "Username is available");

        // Return response with 200 OK
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByName(@PathVariable String userName ){
        return userModelService.getUserByUserName2(userName );
    }

    @GetMapping("/play/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponsePlayShare getUserByName4(@PathVariable String userName ){
        return userModelService.getUserByUserName4(userName );
    }

    @DeleteMapping("/user/delete/{userName}")
    @ResponseStatus(HttpStatus.GONE)
    public String deleteUser(@PathVariable String userName){
        return userModelService.deleteUserByName(userName);
    }



    @PutMapping("/upload-image/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUserImage(@PathVariable String name , @RequestParam("image") MultipartFile file){
        return imageService.updateUserImage(name,file);
    }

    @PutMapping("/set-tag-quotes/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse udpateTagAndQuotes(@PathVariable String name , @RequestBody TagAndQuotesRequest tagAndQuotesRequest) {
        return userModelService.updateQuotesAndTag(name,tagAndQuotesRequest);
    }

}
