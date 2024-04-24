package com.example.bulletinboard.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
	
	private static final long serialVersionUID = -6199706532041231569L;
	private Map<String, String> errorMap;
	
	public CustomException(String message) { 
		super(message);
	}
	public CustomException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
}
