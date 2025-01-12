package com.nguyenminh.microservices.zwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String userName;
    private String company;
    private String password;
    private String emailAddress;
    private String fullName;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String aboutMe;
    private String quotes;
    private String tag;
    private String totalAmount;
    private String friend ;
    private String profileImage;

    @DBRef
    private PaymentSchedule paymentSchedule;

    @DBRef
    private List<TransactionHistory> transactionHistory = new ArrayList<>();

    @DBRef
    private List<Debt> debts = new ArrayList<>();


    @DBRef
    private MoneyPredict moneyPredict;

    @DBRef
    private List<PlayShare> playShares = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserModel user = (UserModel) obj;
        return Objects.equals(id, user.id); // Compare only non-recursive attributes
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash code for non-recursive attributes
    }
}
