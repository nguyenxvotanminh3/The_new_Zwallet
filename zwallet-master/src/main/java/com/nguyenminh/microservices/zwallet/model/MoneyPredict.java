package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "money_predict")
public class MoneyPredict {
    private String id;
    private BigDecimal needs;
    private BigDecimal savings;
    private BigDecimal investment;
    private BigDecimal hobbies;
    private BigDecimal emergency;
    private BigDecimal charity;
    @DBRef
    @JsonIgnore
    private UserModel userModel;
}
