package com.blacknebula.scrumpoker.config;

import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request, HttpServletRequest httpServletRequest) {
        LOGGER.error("Error ", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof CustomException) {
            final CustomException exception = (CustomException) ex;
            httpStatus = HttpStatus.valueOf(exception.getCustomErrorCode().getStatusCode());
        }

        final ModelMap response = new ModelMap();
        response.addAttribute(ErrorResponse.Attributes.DATE_TIME, new Date());
        response.addAttribute(ErrorResponse.Attributes.EXCEPTION, ex.getMessage());
        response.addAttribute(ErrorResponse.Attributes.URI, httpServletRequest.getRequestURI());
        return handleExceptionInternal(ex, response, new HttpHeaders(), httpStatus, request);
    }
}
