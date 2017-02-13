package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.repository.custom.GenericRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends MongoRepository<T, ID>, GenericRepositoryCustom<T, ID> {

}
