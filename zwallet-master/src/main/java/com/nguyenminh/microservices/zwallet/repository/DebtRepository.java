package com.nguyenminh.microservices.zwallet.repository;

import com.nguyenminh.microservices.zwallet.model.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
    Debt findByDebtorId(String userId);
}
