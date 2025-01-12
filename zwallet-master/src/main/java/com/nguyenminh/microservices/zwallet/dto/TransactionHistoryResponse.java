package com.nguyenminh.microservices.zwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {
    private String transactionId;
    private String amountUsed;
    private String category;
    private String moneyLeft;
    private String purpose;
    private String userId;
    @CreatedDate
    private LocalDateTime createdAt;


}
