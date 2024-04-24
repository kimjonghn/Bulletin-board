package com.example.bulletinboard.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.bulletinboard.dto.common.ErrorResponseDto;
import com.example.bulletinboard.exception.CustomException;


@RestControllerAdvice
public class AdviceController {
	@ExceptionHandler(CustomException.class) 
	public ResponseEntity<?> customException(CustomException e) { 
		
		return ResponseEntity.badRequest().body(new ErrorResponseDto<>(e.getMessage(), e.getErrorMap()));
	}
}
