package com.influans.sp.repository;

import com.influans.sp.entity.UserEntity;
import com.influans.sp.entity.def.EntityIdDef;
import com.influans.sp.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<UserEntity, String>, UserRepositoryCustom {

}