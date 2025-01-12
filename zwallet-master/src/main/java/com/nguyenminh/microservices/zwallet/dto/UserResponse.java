package com.nguyenminh.microservices.zwallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.nguyenminh.microservices.zwallet.model.MoneyPredict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private String userId;
    private String userName;
    @JsonIgnoreProperties
    private String password;
    private String company;
    private String emailAddress;
    private String fullName;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String aboutMe;
    private String quotes;
    private String tag;
    private String totalAmount;
    private String authenJwt;
    private String profileImage;
    private MoneyPredictResponse moneyPredictResponse;
    private List<TransactionHistoryResponse> transactionHistoryResponses = new ArrayList<>();
    private List<DebtResponse> debtResponses = new ArrayList<>();
    private List<PlayShareResponse> playShareResponses = new ArrayList<>();
}
