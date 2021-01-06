package com.nagarro.assignment.config.exception;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseExceptionModel {

    private Date timestamp;
    private String message;
    private String error;
    private Integer status;
    private String path;
}
