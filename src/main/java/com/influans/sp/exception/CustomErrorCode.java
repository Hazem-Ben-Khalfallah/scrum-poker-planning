package com.influans.sp.exception;


import javax.ws.rs.core.Response;

public enum CustomErrorCode {
    BAD_ARGS(Response.Status.BAD_REQUEST, "Parameter should not be null or empty"), //
    DUPLICATE_IDENTIFIER(Response.Status.CONFLICT, "An object with the same identifier already exists"), //
    OBJECT_NOT_FOUND(Response.Status.NOT_FOUND, "Object not found"), //
    INTERNAL_SERVER_ERROR(Response.Status.INTERNAL_SERVER_ERROR, "Unmanaged exception"), //
    NOT_YET_IMPLEMENTED(Response.Status.NOT_IMPLEMENTED, "Service not yet implemented"), //
    UNAUTHORIZED(Response.Status.UNAUTHORIZED, "Requires user authentication"), //
    PERMISSION_DENIED(Response.Status.FORBIDDEN, "Rights problem"), //
    SERVICE_UNAVAILABLE(Response.Status.SERVICE_UNAVAILABLE, "Service unavailable"), //
    BAD_GATEWAY(Response.Status.BAD_GATEWAY, "Problem with external api service");

    private final Response.Status code;
    private final String message;

    CustomErrorCode(Response.Status code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response.Status getHttpStatus() {
        return code;
    }

    public int getStatusCode() {
        return code.getStatusCode();
    }

    public String getMessage() {
        return message;
    }

}
