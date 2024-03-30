package com.example.bulletinboard.service;
						
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bulletinboard.dto.auth.LoginReqDto;
import com.example.bulletinboard.dto.auth.SignupReqDto;
import com.example.bulletinboard.entity.Authority;
import com.example.bulletinboard.entity.User;
import com.example.bulletinboard.exception.CustomException;
import com.example.bulletinboard.exception.ErrorMap;
import com.example.bulletinboard.repository.UserRepository;
import com.example.bulletinboard.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	
	public void checkDuplicatedEmail(String email) {
		if(userRepository.findUserByEmail(email) != null) {
			throw new CustomException("Duplicated Email", 
					ErrorMap.builder().put("email", "사용중인 이메일입니다").build());
		}
	}
	
	public void signup(SignupReqDto signupReqDto) {
		
		if(misMathPassword(signupReqDto)) {
			throw new CustomException("error",
					ErrorMap.builder().put("password", "비밀번호가 일치하지 않습니다.").build());
		}
		
		User user = signupReqDto.toEntity();
		
		userRepository.saveUser(user);
		System.out.println(user);
		
		userRepository.saveAuthority(Authority.builder()
				.userId(user.getUserId())
				.roleId(1)
				.build());		
	}
	
	public boolean misMathPassword(SignupReqDto signupReqDto) {
		return !signupReqDto.getPassword().equals(signupReqDto.getCheckPassword());
	}
	
	public String signin(LoginReqDto loginReqDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword());
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//		return null;
		return jwtTokenProvider.generateToken(authentication);
	}
	//jwtTokenProvider 객체를 사용하여 인증된 사용자에 대한 JWT 토큰을 생성합니다. JWT 토큰은 클라이언트에게 반환되고, 이를 사용하여 사용자가 인증된 세션을 유지하거나 보호된 리소스에 접근할 수 있게 됩니다.
	// jwt 토큰을 생성하여 반납하는 역할
	
	public boolean authenticate(String accessToken) {
		return jwtTokenProvider.validateToken(jwtTokenProvider.getToken(accessToken));
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findUserByEmail(username);
		return userEntity.toPrincipal();
	} 
	
}
