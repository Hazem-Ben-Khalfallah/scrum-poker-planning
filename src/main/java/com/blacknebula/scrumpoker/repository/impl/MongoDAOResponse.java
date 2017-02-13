package com.blacknebula.scrumpoker.repository.impl;

import com.blacknebula.scrumpoker.repository.DAOResponse;
import com.mongodb.WriteResult;

public class MongoDAOResponse implements DAOResponse {
    private Integer nAffected;

    public MongoDAOResponse() {

    }

    public MongoDAOResponse(WriteResult result) {
        this.nAffected = result.getN();
    }

    public Integer getnAffected() {
        return this.nAffected;
    }

    public void setnAffected(Integer nAffected) {
        this.nAffected = nAffected;
    }

}
