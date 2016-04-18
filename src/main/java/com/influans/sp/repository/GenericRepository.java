package com.influans.sp.repository;

import com.influans.sp.repository.custom.GenericRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends MongoRepository<T, ID>,  GenericRepositoryCustom<T, ID> {

}
