package com.example.bulletinboard.service;
						
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bulletinboard.dto.auth.LoginReqDto;
import com.example.bulletinboard.dto.auth.PasswordUpdateDto;
import com.example.bulletinboard.dto.auth.SignupReqDto;
import com.example.bulletinboard.entity.Authority;
import com.example.bulletinboard.entity.User;
import com.example.bulletinboard.exception.CustomException;
import com.example.bulletinboard.exception.ErrorMap;
import com.example.bulletinboard.repository.UserRepository;
import com.example.bulletinboard.security.JwtTokenProvider;
import com.example.bulletinboard.security.principalUser;

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

		return jwtTokenProvider.generateToken(authentication);
	}

	
	public boolean authenticate(String accessToken) {
		return jwtTokenProvider.validateToken(jwtTokenProvider.getToken(accessToken));
	}
	
	public String findEmail(String phone) {
			User userEntity = userRepository.findEmail(phone);			
			
			if (userEntity == null) {
				throw new CustomException("Undefind User", 
						ErrorMap.builder().put("error", "사용자를 찾을 수 없습니다.").build());
			}
			
			return userEntity.getEmail();
		
	}
	public int findPassword(Map<String, Object> map) {
		if(userRepository.findPassword(map) != 0) {
			return userRepository.findPassword(map);
		}
		return 0;
		
	}
	public void passwordChange(int userId, PasswordUpdateDto passwordUpdateDto) {
		
		if(misMathUpdatePassword(passwordUpdateDto)) {
			throw new CustomException("error",
					ErrorMap.builder().put("password", "비밀번호가 일치하지 않습니다.").build());
		}
		userRepository.passwordChange(User.builder()
			.userId(userId)
			.password(new BCryptPasswordEncoder().encode(passwordUpdateDto.getPassword()))
			.build());
	}
	public boolean misMathUpdatePassword(PasswordUpdateDto passwordUpdateDto) {
		return !passwordUpdateDto.getPassword().equals(passwordUpdateDto.getCheckPassword());
	}
	
	public principalUser userInfo(String accessToken) {
		String email = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken)).get("email").toString();
		User userEntity = userRepository.findUserByEmail(email);
		return userEntity.toPrincipal();
	}
	public boolean usercheck(String accessToken, String boardId) {
		String email = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken)).get("email").toString();

		if(userRepository.findUserByEmail(email).getUserId() == userRepository.boardUserId(boardId)) {
			return true;
		}
		return false;
	}
	
	public int userDelete(User user) {
		userRepository.userDelete(user);
		return userRepository.userDelete(user);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findUserByEmail(username);
		return userEntity.toPrincipal();
	} 
	
}
