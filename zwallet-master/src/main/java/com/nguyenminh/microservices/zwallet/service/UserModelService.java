package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.*;

import com.nguyenminh.microservices.zwallet.exception.UserNotFoundException;
import com.nguyenminh.microservices.zwallet.model.*;

import com.nguyenminh.microservices.zwallet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserModelService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final EncryptPasswordSerivce encryptPasswordSerivce;
    private final ValidateUserService validateUserService;




    // Update user detail
    public UserResponse updateUserDetail(String name, UserModel userModel2) {
        validateUserService.checkUserIsAcceptToUserApi(name);
        UserModel userModel = userRepository.findByUserName(name);

        if (userModel != null) {
            userModel.setUserName(userModel2.getUserName());
            userModel.setCompany(userModel2.getCompany());
            userModel.setCity(userModel2.getCity());
            userModel.setAddress(userModel2.getAddress());
            userModel.setCountry(userModel2.getCountry());
            userModel.setTag(userModel2.getTag());
            userModel.setAboutMe(userModel2.getAboutMe());
            userModel.setQuotes(userModel2.getQuotes());
            userModel.setPostalCode(userModel2.getPostalCode());
            userModel.setEmailAddress(userModel2.getEmailAddress());
            userModel.setFullName(userModel2.getFullName());
            userRepository.save(userModel);
            return mapper.mapToUserResponse(userModel);
        } else {
            throw new RuntimeException("Cant find user");
        }
    }


    //Create new user
    public UserModel createUser(UserRegistrationDto userRegistrationDto) {
        UserModel userModel = new UserModel();
        userModel.setFullName(userRegistrationDto.getFullName());
        userModel.setEmailAddress(userRegistrationDto.getEmailAddress());
        userModel.setUserName(userRegistrationDto.getUserName());
        userModel.setPassword(encryptPasswordSerivce.encryptPassword(userRegistrationDto.getPassword()));
        userModel.setCity(null);
        userModel.setAddress(null);
        userModel.setCountry(null);
        userModel.setTag(null);
        userModel.setAboutMe(null);
        userModel.setQuotes("Click here to edit the quotes yourself !");
        userModel.setTag(null);
        userModel.setPostalCode(null);
        userModel.setCompany(null);
        userModel.setTotalAmount("0");
        if (userRepository.findByUserName(userRegistrationDto.getUserName()) != null) {
            throw new RuntimeException("This user name has been used!");
        } else {
            userRepository.save(userModel);
            return userModel;
        }
    }


    //Get user by name
    @Transactional
    public UserResponse getUserByUserName(String userName) {
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            // Handle the case where transactionHistories might be null
            return UserResponse.builder()
                    .password(userModel.getPassword())
                    .userName(userModel.getUserName())
                    .build();
        } else {
            throw new UserNotFoundException("Cant find user with user name : " + userName);
        }
    }
    public UserResponsePlayShare getUserByUserName4(String userName) {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        UserModel userModel = userRepository.findByUserName(userName);
        // Handle the case where transactionHistories might be null
        List<PlayShare> playSharesList = userModel.getPlayShares();
        List<PlayShareResponse> playShareResponses = (playSharesList != null)
                ? playSharesList.stream()
                .filter(Objects::nonNull) // Bỏ qua các phần tử null
                .map(mapper::mapToPlayShareResponse)
                .toList()
                :Collections.emptyList();
        return UserResponsePlayShare.builder()
                .playShareResponses(playShareResponses)
                .build();

    }
    public UserResponse getUserByUserName2(String userName) {

        validateUserService.checkUserIsAcceptToUserApi(userName);
        UserModel userModel = userRepository.findByUserName(userName);
            // Handle the case where transactionHistories might be null
            List<TransactionHistory> transactionHistories = userModel.getTransactionHistory();
        List<Debt> debtList = userModel.getDebts();
            List<TransactionHistoryResponse> transactionHistoryResponses = (transactionHistories != null)
                    ? transactionHistories.stream()
                    .filter(Objects::nonNull) // Bỏ qua các phần tử null
                    .map(mapper::mapToTransactionResponse)
                    .toList()
                    : Collections.emptyList();
        List<DebtResponse> debts = (debtList != null)
                ? debtList.stream()
                .filter(Objects::nonNull) // Bỏ qua các phần tử null
                .map(mapper::mapToDebtResponse)
                .toList()
                : Collections.emptyList();

            return UserResponse.builder()

                    .userId(userModel.getId())
                    .company(userModel.getCompany())
                    .quotes(userModel.getQuotes())
                    .country(userModel.getCountry())
                    .postalCode(userModel.getPostalCode())
                    .userName(userModel.getUserName())
                    .city(userModel.getCity())
                    .address(userModel.getAddress())
                    .aboutMe(userModel.getAboutMe())
                    .tag(userModel.getTag())
                    .profileImage(userModel.getProfileImage())
                    .emailAddress(userModel.getEmailAddress())
                    .fullName(userModel.getFullName())
                    .totalAmount(userModel.getTotalAmount())
                    .debtResponses(debts)
                    .transactionHistoryResponses(transactionHistoryResponses)
                    .build();

    }
    public UserResponse getUserByUserName3(String userName) {
        UserModel userModel = userRepository.findByUserName(userName);
        // Handle the case where transactionHistories might be null
        List<Debt> debtList = userModel.getDebts();
        MoneyPredict moneyPredict = userModel.getMoneyPredict();
        MoneyPredictResponse moneyPredictResponse1 =  mapper.maptoMoneyPredictResponse(moneyPredict);
        List<DebtResponse> debts = (debtList != null)
                ? debtList.stream()
                .filter(Objects::nonNull) // Bỏ qua các phần tử null
                .map(mapper::mapToDebtResponse)
                .toList()
                : Collections.emptyList();
        return UserResponse.builder()
                .userId(userModel.getId())
                .debtResponses(debts)
                .build();

    }

    // delete user by name
    public String deleteUserByName(String userName) {
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            userRepository.deleteById(String.valueOf(Integer.valueOf(userModel.getId())));
            return "Deleted user: " + userName;
        } else throw new UserNotFoundException("cant find user");
    }

    // update Quotes of user
    public UserResponse updateQuotesAndTag(String userName, TagAndQuotesRequest tagAndQuotesRequest) {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            userModel.setTag(tagAndQuotesRequest.getTag());
            userModel.setQuotes(tagAndQuotesRequest.getQuotes());
            userRepository.save(userModel);

        } else throw new RuntimeException("Cant find user " + userName);

        return mapper.mapToUserResponse(userModel);
    }


}
