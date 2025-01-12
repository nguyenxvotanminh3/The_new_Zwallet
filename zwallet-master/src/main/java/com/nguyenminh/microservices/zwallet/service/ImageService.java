package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final Mapper mapper;
    public UserResponse updateUserImage(String name, MultipartFile file) {
        String imageData = cloudinaryService.upload(file);
        UserModel userModel = userRepository.findByUserName(name);
        if (userModel != null) {
            userModel.setProfileImage(imageData);
            userRepository.save(userModel);
            return mapper.mapToUserResponse(userModel);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
