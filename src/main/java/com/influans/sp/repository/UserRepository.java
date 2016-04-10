package com.influans.sp.repository;

import com.influans.sp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll();

    List<User> findUsersBySessionId(String sessionId);
}