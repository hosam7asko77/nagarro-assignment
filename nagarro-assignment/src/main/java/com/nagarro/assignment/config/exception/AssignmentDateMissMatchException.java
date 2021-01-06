package com.nagarro.assignment.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AssignmentDateMissMatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	public AssignmentDateMissMatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssignmentDateMissMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	

}
