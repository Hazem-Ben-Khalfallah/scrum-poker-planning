package com.blacknebula.scrumpoker.repository.impl;

import com.blacknebula.scrumpoker.repository.custom.GenericRepositoryCustom;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericRepositoryImpl<T, ID extends Serializable> implements GenericRepositoryCustom<T, ID> {

    protected static final String MONGO_ID = "_id";
    @Autowired
    protected MongoTemplate mongoTemplate;

    public abstract Class<T> getTClass();

    public abstract ID getId(T t);

    @Override
    public T findOne(ID id, List<String> fields) {
        Query q = idQuery(id);
        addProjection(fields, q);
        return this.mongoTemplate.findOne(q, this.getTClass());
    }

    @Override
    public List<T> search(List<String> fields) {
        Query q = new Query();
        addProjection(fields, q);
        return this.mongoTemplate.find(q, this.getTClass());
    }

    private void addProjection(List<String> fields, Query q) {
        if (fields != null) {
            for (String field : fields) {
                q.fields().include(field);
            }
        }
    }

    @Override
    public MongoDAOResponse update(ID id, Map<String, Object> values) {
        Query q = idQuery(id);

        Update up = new Update();
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                up.set(entry.getKey(), entry.getValue());
            }
        }
        UpdateResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }

    @Override
    public MongoDAOResponse update(ID id, String field, Object value) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(field, value);
        return this.update(id, values);
    }

    @Override
    public MongoDAOResponse increment(ID id, String field, Number inc) {
        Query q = idQuery(id);
        Update up = new Update();
        up.inc(field, inc);
        UpdateResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);

    }

    private Query idQuery(ID id) {
        Query q = new Query();
        q.addCriteria(Criteria.where(MONGO_ID).is(id));
        return q;
    }

    @Override
    public T create(T t) {
        this.mongoTemplate.insert(t);
        return t;
    }

    @Override
    public void upsert(T t) {
        this.mongoTemplate.save(t);
    }

}
