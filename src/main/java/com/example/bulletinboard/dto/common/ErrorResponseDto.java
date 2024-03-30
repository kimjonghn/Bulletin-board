package com.example.bulletinboard.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto<T> {
	private String message;
	private T errorData; //(errorMap)
}
