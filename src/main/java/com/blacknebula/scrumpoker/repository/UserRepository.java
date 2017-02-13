package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.entity.EntityId;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, EntityId>, UserRepositoryCustom {

}