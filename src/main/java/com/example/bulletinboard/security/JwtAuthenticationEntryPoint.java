package com.example.bulletinboard.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.bulletinboard.dto.common.ErrorResponseDto;
import com.example.bulletinboard.exception.ErrorMap;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); 
		
		ErrorResponseDto<?> errorResponseDto =
				new ErrorResponseDto<AuthenticationException>("",authException);
		if(authException.getClass() == BadCredentialsException.class || authException.getClass() == InternalAuthenticationServiceException.class) {
			errorResponseDto = new ErrorResponseDto<>("로그인 실패", ErrorMap.builder().put("email", "사용자 정보가 일치하지 않습니다.").build());
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String responseJson = objectMapper.writeValueAsString(errorResponseDto);
		PrintWriter out = response.getWriter();
		out.println(responseJson);
	}
}
