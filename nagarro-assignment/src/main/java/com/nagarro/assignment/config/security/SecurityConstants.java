package com.nagarro.assignment.config.security;


public class SecurityConstants {
	
	
//	public static final long EXPIRATION_TIME = 864000000; //10days
	public static final long EXPIRATION_TIME = 300000;//1 hour 
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String KEY = "nagarro1234";
	public static final String HEADER_STRING = "Authorization";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLES = "roles";
	public static final String EXPIRATION_TIME_ERROR = "Token has expired";
	public static final String EXPIRED_MESSAGE = "this token is not available";
	public static final String TOKEN_ERROR = "token invalid";
	public static final String TOKEN_MESSAGE = "this token is invalid or changed";
}
