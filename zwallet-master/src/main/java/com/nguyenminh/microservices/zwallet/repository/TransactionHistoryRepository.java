package com.nguyenminh.microservices.zwallet.repository;


import com.nguyenminh.microservices.zwallet.model.TransactionHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory, String> {
}
