package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PercentUsage {
    private double Food;
    private double Bill;
    private double Entertain;
    private double Shopping;
    private double Investment;
    private double Medicine;
    private double Education;
    private double Travel;
    private double Rent;
    private double Transportation;
    private double Utilities;
    private double Savings;
    private double Charity;
    private double Insurance;
    private double Gifts;
    private double Recive;
    private double Transfer;
    private double Others;
}
