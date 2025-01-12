package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.TransactionHistoryRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;
    private final ValidateUserService validateUserService;

    public UserModel updateTransactionHistory(String name, TransactionHistory transactionHistory){
        validateUserService.checkUserIsAcceptToUserApi(name);
        UserModel userResponse = userRepository.findByUserName(name);
        List<TransactionHistory> transactionHistoryList = userResponse.getTransactionHistory();
        if (transactionHistoryList == null) {
            transactionHistoryList = new ArrayList<>();
        }
        transactionHistory.setUser(userResponse);
        transactionHistoryRepository.save(transactionHistory);
        transactionHistoryList.add(transactionHistory);
        return userRepository.save(userResponse);

    }
}
