package com.influans.sp.dto;

import com.influans.sp.enums.WsTypes;

/**
 * @author hazem
 */
public class WsRequest {
    private WsTypes type;
    private Object data;

    public WsRequest(WsTypes type, Object data) {
        this.data = data;
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public WsTypes getType() {
        return type;
    }

    public void setType(WsTypes type) {
        this.type = type;
    }
}
