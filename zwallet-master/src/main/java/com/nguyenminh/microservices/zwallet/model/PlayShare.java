package com.nguyenminh.microservices.zwallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "playshare")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayShare {
    private String id;
    private String title;
    private String totalAmount;
    private Map<String , Map<String, Boolean>> userOwedAmounts;

    // Thời gian đóng tiền của từng user
    private Map<String ,Map<String, String>> paymentHistory;


    private Boolean status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @DBRef
    private List<UserModel> participants;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayShare playShare = (PlayShare) obj;
        return Objects.equals(id, playShare.id); // Compare only non-recursive attributes
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash code for non-recursive attributes
    }
}

