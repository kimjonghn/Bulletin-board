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
		//HTTP content-type 을 설정하여 클라이 언트 한테 전달하는 데이터 방식
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); //클라이언트가 인증되지 않았을때 사용되는 코드 401코드로 응답
		
		ErrorResponseDto<?> errorResponseDto =
				new ErrorResponseDto<AuthenticationException>("",authException);
		// AuthenticationException 이것은 제네릭 타입으로 객체를 전달
		// authException 클라이언트가 인증되지 않았거나 인증에 실패했을 때 발생한 예외를 나타내는 객체
		//authException 그럼 이부분이 어떤에러인지 판단하고 "" 여기에 알맞은 에러문구를 띄어 주는거다
		if(authException.getClass() == BadCredentialsException.class || authException.getClass() == InternalAuthenticationServiceException.class) {
			errorResponseDto = new ErrorResponseDto<>("로그인 실패", ErrorMap.builder().put("email", "사용자 정보가 일치하지 않습니다.").build());
		}
		// 잘 모르겠음 일단 로그인 실패시 실행이 되는 코드이다
		ObjectMapper objectMapper = new ObjectMapper();
		String responseJson = objectMapper.writeValueAsString(errorResponseDto);
		//  ErrorResponseDto 객체를 JSON 형식의 문자열로 변환하는 작업을 수행
		PrintWriter out = response.getWriter();
		out.println(responseJson);
		// HttpServletResponse 객체에서 PrintWriter를 가져와서 JSON 형식의 응답을 클라이언트에게 보내는 부분입니다.
	}
}
