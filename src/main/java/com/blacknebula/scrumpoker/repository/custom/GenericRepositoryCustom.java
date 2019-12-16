package com.blacknebula.scrumpoker.repository.custom;


import com.blacknebula.scrumpoker.repository.DAOResponse;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericRepositoryCustom<T, ID extends Serializable> {

    /**
     * Map the results of an id query on the collection for the entity class to a single instance of an object of the
     * specified type.
     *
     * @param id     entityId
     * @param fields projections
     * @return T
     * @should return unique entity with selected fields if Id exists
     * @should return null id id is not valid
     */
    T findOne(ID id, List<String> fields);

    /**
     * Map all documents on the collection for the entity class to a List of the specified type.
     *
     * @param fields projections
     * @return List of T
     * @should return all documents with selected fields
     * @should return empty list if collection is empty
     */
    List<T> search(List<String> fields);

    /**
     * Insert the object into the collection for the entity type of the object to save.
     * <p/>
     * If your object has an "Id' property, it will be set with the generated Id from MongoDB.
     *
     * @param t the object to store in the collection.
     * @return the object  with the new Id
     * @should insert the object in the collection
     * @should generate a new id even if the id field is not set
     */
    T create(T t);

    /**
     * Insert or update the object into the collection for the entity type of the object to save.
     * <p/>
     * If your object has an "Id' property and it already exists in the related collection, the object wil be updated.
     * If not,it will be created.
     *
     * @param t the object to store in the collection.
     * @should insert the object in the collection if Id is not set
     * @should insert the object in the collection if Id is set but not found in the collection
     * @should update the object in the collection if Id is set and exists in the collection
     */
    void upsert(T t);

    /**
     * Updates the object that is found in the collection of the entity class that matches ths given id with
     * the provided update document.
     * </p>
     * this method is the same as {@link #update(ID, Map)}  update(ID id, Map<String, Object> values)} but it updates only one field
     *
     * @param id    entity id
     * @param field field to update
     * @param value value to be set
     * @return MongoDAOResponse
     * @should update field on selected document with given value
     */
    DAOResponse update(ID id, String field, Object value);

    /**
     * Updates the object that is found in the collection of the entity class that matches ths given id with
     * the provided update values.
     *
     * @param id     entity id
     * @param values values to update
     * @return MongoDAOResponse
     * @should update selected document with given values
     * @should not perform an update if id does not exists
     */
    DAOResponse update(ID id, Map<String, Object> values);

    /**
     * increments a numeric fields with a given value.
     *
     * @param id    entity id
     * @param field numeric field to increment
     * @param inc   value to add
     * @return MongoDAOResponse
     * @should increment numeric field with given value
     */
    DAOResponse increment(ID id, String field, Number inc);

}
