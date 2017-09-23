package com.weddingservices.exceptions;


@SuppressWarnings("serial")
public class CustomizeException extends RuntimeException{
	
	private Object obj;

    public CustomizeException(String message, Object obj) {
        super(message);
        this.obj = obj;
    }

    public Object getObj() { return obj; }
	
}
