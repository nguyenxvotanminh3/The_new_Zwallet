package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.TagAndQuotesRequest;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.exception.UserNotFoundException;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final ValidateUserService validateUserService;

    // update quotes
    public UserResponse updateQuotesAndTag(String userName, TagAndQuotesRequest tagAndQuotesRequest) {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            userModel.setTag(tagAndQuotesRequest.getTag());
            userModel.setQuotes(tagAndQuotesRequest.getQuotes());
            userRepository.save(userModel);

        } else throw new UserNotFoundException("Cant find user ");

        return mapper.mapToUserResponse(userModel);
    }

    // Change total field of User model
    public UserResponse updateUserTotal(String name, String totalAmount) {
        validateUserService.checkUserIsAcceptToUserApi(name);
        UserModel userModel = userRepository.findByUserName(name);
        if (userModel != null) {
            userModel.setTotalAmount(totalAmount);
            userRepository.save(userModel);
        } else {
            throw new UserNotFoundException("Cant find user");
        }
        return mapper.mapToUserResponse(userModel);
    }


}
