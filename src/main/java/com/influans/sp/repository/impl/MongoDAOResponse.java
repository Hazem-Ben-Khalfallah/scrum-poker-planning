package com.influans.sp.repository.impl;

import com.mongodb.WriteResult;

public class MongoDAOResponse {
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
