package com.nguyenminh.microservices.zwallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(value = "debt")
public class Debt {
    private String id;
    private String amount;
    private String content;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @DBRef
    private UserModel creator;  // Người tạo khoản nợ
    @DBRef
    private UserModel debtor;   // Người nợ
    private boolean confirmed;  // Trạng thái xác nhận của khoản nợ
}
