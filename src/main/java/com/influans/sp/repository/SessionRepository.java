package com.influans.sp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.influans.sp.entity.Session;

public interface SessionRepository extends MongoRepository<Session, String> {
	Session findBySessionId(String sessionid);
	List<Session> findAll();
}