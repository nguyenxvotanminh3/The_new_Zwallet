package com.nguyenminh.microservices.zwallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String paymentScheduleId;
    private double paymentAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    private int paymentAdvanceMonths; // Số tháng trả trước

}
