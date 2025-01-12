package com.nguyenminh.microservices.zwallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(value = "payment_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule {
    private String id;
    private String serviceName;
    private double amountPerCycle;
    private double totalPaid;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextDueDate;

    private String userId;
    private int paymentAdvanceMonths; // Số tháng đóng trước

    private List<Map<String, String>> paymentHistory; // paymentHistory chứa danh sách các Map // Lịch sử các ngày đóng tiền

    // Getter và Setter cho paymentHistory
    public void setPaymentHistory(List<Map<String, String>> paymentHistory) {
        List<Map<String, String>> validPaymentHistory = new ArrayList<>();
        for (Map<String, String> record : paymentHistory) {
            if (record != null && record.containsKey("date") && record.containsValue("done")) {
                validPaymentHistory.add(record);
            }
        }
        this.paymentHistory = validPaymentHistory;
    }
}


