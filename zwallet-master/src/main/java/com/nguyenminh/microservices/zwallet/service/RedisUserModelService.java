package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service

public class RedisUserModelService {
    private static final String HASH_KEY = "USER_MODEL";

    private final HashOperations<Object, String, UserModel> hashOperations;

    @Autowired
    public RedisUserModelService(RedisTemplate<Object, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(UserModel userModel) {
        hashOperations.put(HASH_KEY, userModel.getId(), userModel);
    }

    public Map<String, UserModel> findAll() {
        return hashOperations.entries(HASH_KEY);
    }

    public UserModel findById(String id) {
        return hashOperations.get(HASH_KEY, id);
    }

    public void update(UserModel userModel) {
        save(userModel);
    }

    public void delete(String id) {
        hashOperations.delete(HASH_KEY, id);
    }
}
