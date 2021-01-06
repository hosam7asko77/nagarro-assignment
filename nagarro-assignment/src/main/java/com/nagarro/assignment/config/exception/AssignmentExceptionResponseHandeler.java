
package com.nagarro.assignment.config.exception;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class AssignmentExceptionResponseHandeler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> hanaleAllException(Exception ex, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError(ex.getMessage());
        exceptionModel.setMessage(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        exceptionModel.setPath(request.getContextPath());
        return new ResponseEntity<>(exceptionModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AssignmentNotFoundApiException.class)
    public final ResponseEntity<Object> hanaleNotFoundException(Exception ex, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError(ex.getMessage());
        exceptionModel.setMessage(request.getDescription(true));
        exceptionModel.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionModel.setPath(request.getDescription(false));
        return new ResponseEntity<>(exceptionModel, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Validation failed some arguments are missing");
        exceptionModel.setMessage(request.getDescription(true));
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
    }

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Not Support Media Type");
        exceptionModel.setMessage(ex.getSupportedMediaTypes().toString());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Not support method See the details");
        exceptionModel.setMessage(ex.getMethod());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Media type not acceptable");
        exceptionModel.setMessage(ex.getSupportedMediaTypes().toString());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Missing path variable please add the variable");
        exceptionModel.setMessage(ex.getVariableName());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("You have missied some parameter"+ex.getMessage());
        exceptionModel.setMessage(ex.getParameterName());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
	}


	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Type Mismatch error plase check the body or request parametar");
        exceptionModel.setMessage(Objects.requireNonNull(ex.getValue()).toString());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.NOT_ACCEPTABLE);
	}
	@ExceptionHandler(AssignmentDateMissMatchException.class)
	public final ResponseEntity<Object> handleMyMismatch(Exception ex, WebRequest request) {
        ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
        exceptionModel.setTimestamp(new Date());
        exceptionModel.setError("Data Not match error");
        exceptionModel.setMessage(ex.getMessage());
        exceptionModel.setPath(request.getDescription(false));
        exceptionModel.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(exceptionModel, HttpStatus.NOT_ACCEPTABLE);
	}
	

}
