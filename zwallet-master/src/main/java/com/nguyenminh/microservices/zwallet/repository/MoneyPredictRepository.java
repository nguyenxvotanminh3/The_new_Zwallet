package com.nguyenminh.microservices.zwallet.repository;

import com.nguyenminh.microservices.zwallet.model.MoneyPredict;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyPredictRepository extends MongoRepository<MoneyPredict, String> {
}
