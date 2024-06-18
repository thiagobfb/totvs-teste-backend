package com.thiagobfb.totvstestebackend.exceptions;

public class AuthorizationException extends RuntimeException {
	
	private static final long serialVersionUID = 7402992332576922804L;

	public AuthorizationException(String msg) {
		super(msg);
	}
	
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
