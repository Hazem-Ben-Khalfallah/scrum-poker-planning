package com.influans.sp.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.influans.sp.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
	List<User> findBySessionId(String sessionid);
	List<User> findAll();
	List<User> findUsersBySessionId(String sessionid);
}