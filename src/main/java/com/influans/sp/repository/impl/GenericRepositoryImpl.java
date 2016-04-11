package com.influans.sp.repository.impl;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GenericRepositoryImpl<T, ID extends Serializable> {

    protected static final String MONGO_ID = "_id";
    @Autowired
    protected MongoTemplate mongoTemplate;

    public abstract Class<T> getTClass();

    public abstract ID getId(T t);

    protected String getCollectionName() {
        return mongoTemplate.getCollectionName(getTClass());
    }

    protected DBCollection getCollection() {
        return mongoTemplate.getCollection(getCollectionName());
    }

    protected DBObject toDbObject(T entity) {
        if (!(entity instanceof String)) {
            return (DBObject) mongoTemplate.getConverter().convertToMongoType(entity);
        } else {
            try {
                return (DBObject) JSON.parse((String) entity);
            } catch (JSONParseException e) {
                throw new MappingException("Could not parse given String to save into a JSON document!", e);
            }
        }
    }


    public T findOne(ID id, List<String> fields) {
        Query q = idQuery(id);
        addProjection(fields, q);
        return this.mongoTemplate.findOne(q, this.getTClass());
    }


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


    public MongoDAOResponse update(ID id, Map<String, Object> values) {
        Query q = idQuery(id);

        Update up = new Update();
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                up.set(entry.getKey(), entry.getValue());
            }
        }
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }


    public MongoDAOResponse increment(ID id, String field, Number inc) {
        Query q = idQuery(id);
        Update up = new Update();
        up.inc(field, inc);
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);

    }


    public MongoDAOResponse arrayPush(ID id, String field, Object obj) {
        return arrayPush(idQuery(id), field, obj);
    }


    public MongoDAOResponse arrayPush(String idField, ID id, String field, Object obj) {
        Query q = new Query();
        q.addCriteria(Criteria.where(idField).is(id));
        return arrayPush(q, field, obj);
    }

    private MongoDAOResponse arrayPush(Query q, String field, Object obj) {
        Update up = new Update();
        if (obj != null) {
            up.push(field, obj);
        }
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }


    public MongoDAOResponse arraynPush(ID id, String field, List<?> obj) {
        Query q = idQuery(id);
        Update up = new Update();
        if (obj != null) {
            up.pushAll(field, obj.toArray());
        }
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }


    public MongoDAOResponse addToSet(ID id, String field, Object obj) {
        Query q = idQuery(id);
        Update up = new Update();
        if (obj != null) {
            up.addToSet(field, obj);
        }
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }


    public MongoDAOResponse arrayPull(ID id, String field, Object obj) {
        Query q = idQuery(id);
        Update up = new Update();
        if (obj != null) {
            up.pull(field, obj);
        }
        WriteResult result = this.mongoTemplate.updateFirst(q, up, this.getTClass());
        return new MongoDAOResponse(result);
    }


    public T create(T t) {
        this.mongoTemplate.insert(t);
        return t;
    }


    public MongoDAOResponse update(ID id, String field, Object value) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(field, value);
        return this.update(id, values);
    }


    public void upsert(T t) {
        this.mongoTemplate.save(t);
    }

    private Query idQuery(ID id) {
        Query q = new Query();
        q.addCriteria(Criteria.where(MONGO_ID).is(id));
        return q;
    }


    public BulkBuilder bulk() {
        return new BulkBuilder();
    }

    public class BulkBuilder {
        private BulkWriteOperation bulk;

        public BulkBuilder() {
            bulk = getCollection().initializeOrderedBulkOperation();
        }

        public BulkBuilder insert(List<T> entities) {
            bulkAll(entities, (T t) -> {
                bulk.insert(toDbObject(t));
            });
            return this;
        }

        public BulkBuilder update(List<T> entities) {
            bulkAll(entities, (T t) -> {
                DBObject dbO = toDbObject(t);
                bulk.find(new BasicDBObject(MONGO_ID, dbO.get(MONGO_ID))).updateOne(new BasicDBObject("$set", dbO));
            });
            return this;
        }

        public BulkBuilder upsert(List<T> entities) {
            bulkAll(entities, (T t) -> {
                DBObject dbO = toDbObject(t);
                bulk.find(new BasicDBObject(MONGO_ID, dbO.get(MONGO_ID))) //
                        .upsert() //
                        .updateOne(new BasicDBObject("$set", dbO));
            });
            return this;
        }

        private void bulkAll(List<T> entities, Consumer<T> func) {
            Assert.notEmpty(entities);
            entities.stream().forEach(t -> {
                func.accept(t);
            });
        }

        public BulkWriteResult execute() {
            return bulk.execute();
        }
    }
}
