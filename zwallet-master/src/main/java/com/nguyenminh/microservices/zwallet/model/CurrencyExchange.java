package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "currency_exchange")
public class CurrencyExchange {
    private String id;
    private String currencyFrom;
    private String currencyTo ;
    private BigDecimal conversionMultiple;
}
