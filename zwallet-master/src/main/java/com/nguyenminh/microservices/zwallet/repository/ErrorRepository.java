package com.nguyenminh.microservices.zwallet.repository;

import com.nguyenminh.microservices.zwallet.model.Error;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends MongoRepository<Error, String> {
}
