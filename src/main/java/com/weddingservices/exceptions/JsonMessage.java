package com.weddingservices.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonMessage {
	private int state;
    private String message;

    public JsonMessage() { }

    public JsonMessage(int code, String message) {
        this.state = code;
        this.message = message;
    }

    public int getState() { return state; }

    public void setState(int code) { this.state = code; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

	
}
