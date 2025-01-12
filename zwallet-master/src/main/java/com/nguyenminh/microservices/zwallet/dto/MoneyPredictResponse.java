package com.nguyenminh.microservices.zwallet.dto;

import com.nguyenminh.microservices.zwallet.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MoneyPredictResponse {
    private String id;
    private BigDecimal needs;
    private BigDecimal savings;
    private BigDecimal investment;
    private BigDecimal hobbies;
    private BigDecimal emergency;
    private BigDecimal charity;
    private String userId;
}
