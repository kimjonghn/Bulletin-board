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
			http.cors(); //다른 출처에 요청에 대한 처리가 허용
			http.csrf().disable(); //우리가 만들고자 하는 rest api에서는 csrf 공격으로부터 안전하고 매번 api 요청으로부터 csrf 토큰을 받지 않아도 되어 이 기능을 disable() 하는 것이 더 좋은 판단으로 보인다.
			
			http.authorizeRequests()
				.antMatchers("/auth/**") //auth로 시작하는 url은 아무나 들어올수 있음
				.permitAll() //인정 되던 안되던 모든 사용자를 허용한다
				.anyRequest()
				.authenticated() //이외의 url 은 인증은 거친 자만 들어올수 있음
				.and()
			 	.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // jwt를 통해 사용자 인증
			    .exceptionHandling() // 인증이 실패, 인증되지 않은 사용자 접근 할 경우 예외처리 발생
			    .authenticationEntryPoint(jwtAuthenticationEntryPoint); // 인증이 실패한 경우 예외 처리 발생
		}
}
