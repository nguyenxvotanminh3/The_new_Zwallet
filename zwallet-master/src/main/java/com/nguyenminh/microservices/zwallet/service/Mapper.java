package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.*;
import com.nguyenminh.microservices.zwallet.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Mapper {

    public MoneyPredictResponse maptoMoneyPredictResponse(MoneyPredict moneyPredict) {
        if (moneyPredict == null) {
            return MoneyPredictResponse.builder()
                    .needs(null)
                    .charity(null)
                    .hobbies(null)
                    .savings(null)
                    .investment(null)
                    .userId(null)
                    .build();

        }
        return MoneyPredictResponse.builder()
                .needs(moneyPredict.getNeeds())
                .charity(moneyPredict.getCharity())
                .emergency(moneyPredict.getEmergency())
                .hobbies(moneyPredict.getHobbies())
                .investment(moneyPredict.getInvestment())
                .savings(moneyPredict.getSavings())
                .userId(moneyPredict.getId())
                .build();
    }

    public UserResponse mapToUserResponse(UserModel userModel) {
        List<TransactionHistoryResponse> transactionHistoryResponses = userModel.getTransactionHistory() != null
                ? userModel.getTransactionHistory().stream()
                .map(this::mapToTransactionResponse)
                .toList()
                : Collections.emptyList();
        List<DebtResponse> debtResponses = userModel.getDebts() != null
                ? userModel.getDebts().stream()
                .map(this::mapToDebtResponse)
                .toList()
                : Collections.emptyList();
        List<PlayShareResponse> playShareResponses = userModel.getPlayShares() != null
                ? userModel.getPlayShares().stream()
                .map(this::mapToPlayShareResponse)
                .toList()
                : Collections.emptyList();
        return UserResponse.builder()
                .userId(userModel.getId())
                .company(userModel.getCompany())
                .password(userModel.getPassword())
                .userName(userModel.getUserName())
                .emailAddress(userModel.getEmailAddress())
                .fullName(userModel.getFullName())
                .address(userModel.getAddress())
                .city(userModel.getCity())
                .country(userModel.getCountry())
                .profileImage(userModel.getProfileImage())
                .postalCode(userModel.getPostalCode())
                .aboutMe(userModel.getAboutMe())
                .quotes(userModel.getQuotes())
                .tag(userModel.getTag())
                .totalAmount(userModel.getTotalAmount())
                .transactionHistoryResponses(transactionHistoryResponses)
                .debtResponses(debtResponses)
                .playShareResponses(playShareResponses)
                .build();
    }



    public TransactionHistoryResponse mapToTransactionResponse(TransactionHistory transactionHistory) {
        // Check if transactionHistory is null and return a default response
        if (transactionHistory == null) {
            return TransactionHistoryResponse.builder()
                    .transactionId(null)          // Or some default value
                    .amountUsed("0")             // Or some default value
                    .purpose("N/A")              // Or some default value
                    .moneyLeft("0")              // Or some default value
                    .userId(null)// Or some default value
                    .build();
        }

        // Safely map fields from TransactionHistory to TransactionHistoryResponse
        return TransactionHistoryResponse.builder()
                .transactionId(transactionHistory.getId())
                .amountUsed(transactionHistory.getAmountUsed())
                .category(transactionHistory.getCategory())
                .createdAt(transactionHistory.getCreatedAt())
                .purpose(transactionHistory.getPurpose())
                .moneyLeft(transactionHistory.getMoneyLeft())
                .userId(transactionHistory.getUser() != null ? transactionHistory.getUser().getId() : null) // Handle potential null user
                .build();
    }

    public DebtResponse mapToDebtResponse(Debt debt) {
        // Check if transactionHistory is null and return a default response
        if (debt == null) {
            return DebtResponse.builder()
                    .id(null)          // Or some default value
                    .amount("0")
                    .debtor("")
                    .creator("")
                    .content("N/A")
                    .confirmed(false)// Or some default value// Or some default value
                    .build();
        }

        // Safely map fields from TransactionHistory to TransactionHistoryResponse
        return DebtResponse.builder()
                .id(debt.getId())
                .amount(debt.getAmount())
                .debtor(debt.getDebtor().getUserName())
                .creator(debt.getCreator().getUserName())
                .content(debt.getContent())
                .confirmed(debt.isConfirmed())
                .createdAt(debt.getCreatedAt())
                .build();
    }

    public PlayShareResponse mapToPlayShareResponse(PlayShare playShare) {
        if (playShare == null) {
            Map<String, Map<String, Boolean>> userOwner = new HashMap<>();
            return PlayShareResponse.builder()
                    .id(null)          // Or some default value
                    .totalAmount("0")
                    .title("")
                    .status(false)
                    .userOwedAmounts(userOwner)
                    .build();
        }

        // Safely map fields from TransactionHistory to TransactionHistoryResponse
        return PlayShareResponse.builder()
                .id(playShare.getId())          // Or some default value
                .totalAmount(playShare.getTotalAmount())
                .title(playShare.getTitle())
                .status(playShare.getStatus())
                .userOwedAmounts(playShare.getUserOwedAmounts())
                .createdAt(playShare.getCreatedAt())
                .updatedAt(playShare.getUpdatedAt())
                .build();
    }
}
