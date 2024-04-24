package com.example.bulletinboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bulletinboard.security.JwtAuthenticationEntryPoint;
import com.example.bulletinboard.security.JwtAuthenticationFilter;
import com.example.bulletinboard.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean // 의존성 주입
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors();
			http.csrf().disable();
			
			http.authorizeRequests()
				.antMatchers("/auth/**") 
				.permitAll() 
				.anyRequest()
				.authenticated() 
				.and()
			 	.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) 
			    .exceptionHandling() 
			    .authenticationEntryPoint(jwtAuthenticationEntryPoint); 
		}
}
