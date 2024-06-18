package com.thiagobfb.totvstestebackend.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6683813502562562555L;
	
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
