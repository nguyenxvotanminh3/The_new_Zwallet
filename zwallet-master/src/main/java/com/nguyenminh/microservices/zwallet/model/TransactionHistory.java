package com.nguyenminh.microservices.zwallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(value = "transaction_history")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionHistory {


    private String id;
    private String amountUsed;
    private String category;
    @CreatedDate
    private LocalDateTime createdAt;
    private String purpose;
    private String moneyLeft;
    @DBRef
    @JsonIgnore
    private UserModel user; // Use @DBRef to reference the user


}
