package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PercentUsageTotal {
    private BigDecimal Food;
    private BigDecimal Bill;
    private BigDecimal Entertain;
    private BigDecimal Shopping;
    private BigDecimal Investment;
    private BigDecimal Medicine;
    private BigDecimal Education;
    private BigDecimal Travel;
    private BigDecimal Rent;
    private BigDecimal Transportation;
    private BigDecimal Utilities;
    private BigDecimal Savings;
    private BigDecimal Charity;
    private BigDecimal Insurance;
    private BigDecimal Gifts;
    private BigDecimal Recive;
    private BigDecimal Transfer;
    private BigDecimal Others;
}
