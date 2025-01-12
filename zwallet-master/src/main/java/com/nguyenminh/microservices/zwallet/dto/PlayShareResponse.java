package com.nguyenminh.microservices.zwallet.dto;

import com.nguyenminh.microservices.zwallet.model.UserModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class PlayShareResponse {
    private String id;
    private String title;
    private String totalAmount;
    private Map<String , Map<String , Boolean>> userOwedAmounts;
    private Boolean status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @DBRef
    private List<UserModel> participants;
}
