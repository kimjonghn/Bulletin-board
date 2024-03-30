package com.example.bulletinboard.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter{

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override //HTTP 요청을 처리하고, 필터 체인을 따라서 다음 필터로 요청
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request; // request 객체가 HttpServletRequest 타입으로 형변환
		String accessToken = httpRequest.getHeader("Authorization"); //HTTP요청 헤더에서 Authorization플드의 값을 가져옴
		accessToken = jwtTokenProvider.getToken(accessToken);
		// Bearer을 제거한 예를 들어 "ibmFtZSI6IkpvaG4gRG9" 이런식으로 가져옴
		boolean validationFlag = jwtTokenProvider.validateToken(accessToken);
		// accessToken의 유효성 검사를하는 역할
		if(validationFlag) { //토큰이 유효할 경우 실행이 됨
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
			// 사용자 인증 정보를 가져옴, 토큰을 파싱하고 사용자 인증 정보를 추출하는 역할을 함
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// 시큐리티 컨텍스트는 현재 사용자의 인증 정보를 포함한 객체이다.
			// 스프링 시큐리티는 현재 사용자를 해당 인증 정보로 인증하고, 이를 기반으로 인가 등의 처리를 수행
		}
		
		chain.doFilter(request, response);
		// 다음 필터가 실행됨 다음필터가 없을 경우 컨트롤러나 서블릿이 실행됨
	}
}
