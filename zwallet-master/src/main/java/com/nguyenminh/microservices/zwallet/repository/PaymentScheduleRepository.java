package com.nguyenminh.microservices.zwallet.repository;

import com.nguyenminh.microservices.zwallet.model.PaymentSchedule;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends MongoRepository<PaymentSchedule,String > {
    List<PaymentSchedule> findByUserId(String userId);
}
