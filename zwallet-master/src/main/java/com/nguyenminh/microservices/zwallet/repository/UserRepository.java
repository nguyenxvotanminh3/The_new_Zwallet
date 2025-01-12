package com.nguyenminh.microservices.zwallet.repository;


import com.nguyenminh.microservices.zwallet.model.UserModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel,String> {

    UserModel findByUserName(String userName);

    Optional<UserModel> findByEmailAddress(String email);

    List<UserModel> findAllByEmailAddress(String email);
}
