package com.influans.sp.repository.custom;


import com.influans.sp.repository.DAOResponse;
import com.influans.sp.repository.impl.GenericRepositoryImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericRepositoryCustom<T, ID extends Serializable> {

    T findOne(ID id, List<String> fields);

    List<T> search(List<String> fields);

    T create(T t);

    DAOResponse update(ID id, String field, Object value);

    DAOResponse update(ID id, Map<String, Object> values);

    void upsert(T t);

    DAOResponse increment(ID id, String field, Number inc);

    DAOResponse arrayPush(ID id, String field, Object obj);

    DAOResponse arrayPush(String idField, ID id, String field, Object obj);

    DAOResponse arraynPush(ID id, String field, List<?> obj);

    DAOResponse addToSet(ID id, String field, Object obj);

    DAOResponse arrayPull(ID id, String field, Object obj);

    GenericRepositoryImpl<T, ID>.BulkBuilder bulk();
}
