package com.nguyenminh.microservices.zwallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@Builder
public class DebtResponse {
    private String id;
    private String amount;
    private String content;
    @CreatedDate
    private LocalDateTime createdAt;
    private String creator;  // Người tạo khoản nợ
    private String debtor;   // Người nợ
    private boolean confirmed;

}
