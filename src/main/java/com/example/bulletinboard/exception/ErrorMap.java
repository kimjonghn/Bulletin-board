package com.example.bulletinboard.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ErrorMap {
	private Map<String, String> errorMap;
	
	private ErrorMap() {
		errorMap = new HashMap<>();
	}
	public static ErrorMap builder() {
		return new ErrorMap();
	}
	public ErrorMap put(String key, String value) {
		errorMap.put(key, value);
		return this;
	}
	public Map<String, String> build(){
		return errorMap;
	}
}
