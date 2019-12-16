package com.blacknebula.scrumpoker.dto;

import com.blacknebula.scrumpoker.enums.ResponseStatus;

/**
 * @author hazem
 */
public class DefaultResponse {

    private final ResponseStatus status;

    public DefaultResponse(ResponseStatus status) {
        this.status = status;
    }

    public static DefaultResponse ok() {
        return new DefaultResponse(ResponseStatus.OK);
    }

    public ResponseStatus getStatus() {
        return status;
    }

}
