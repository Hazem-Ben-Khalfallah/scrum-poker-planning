package com.influans.sp.dto;

import com.influans.sp.enums.ResponseStatus;

/**
 * @author hazem
 */
public class DefaultResponse {

    private ResponseStatus status;

    public DefaultResponse() {
    }

    public DefaultResponse(ResponseStatus status) {
        this.status = status;
    }

    public static DefaultResponse ok(){
        return new DefaultResponse(ResponseStatus.OK);
    }

    public static DefaultResponse ko(){
        return new DefaultResponse(ResponseStatus.OK);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
