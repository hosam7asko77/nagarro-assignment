package com.nagarro.assignment.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmentNotFoundApiException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 297279062037839659L;

    public AssignmentNotFoundApiException(String message) {
        super(message);

    }

}
