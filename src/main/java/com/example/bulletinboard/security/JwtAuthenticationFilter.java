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
	
	@Override 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request; 
		String accessToken = httpRequest.getHeader("Authorization"); 
		accessToken = jwtTokenProvider.getToken(accessToken);
		boolean validationFlag = jwtTokenProvider.validateToken(accessToken);
		if(validationFlag) {
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		
		}
		
		chain.doFilter(request, response);
	}
}
