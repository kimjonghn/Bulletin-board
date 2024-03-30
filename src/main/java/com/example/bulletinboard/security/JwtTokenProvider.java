package com.example.bulletinboard.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.bulletinboard.dto.auth.JwtRespDto;
import com.example.bulletinboard.entity.User;
import com.example.bulletinboard.exception.CustomException;
import com.example.bulletinboard.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j //로깅 기능 자동생성
@Component //클래스 자체를 bean 으로 등록
public class JwtTokenProvider {
	
	@Autowired // 의존성 주입
	private UserRepository userRepository;
	private final Key key;
	//의존성 주입이 없다면 의존성을 직접 관리 해야함
	//private UserRepository userRepository = new UserRepository();
	//private final Key key = new Key();

	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		
	}// JWT 토큰을 생성하고 관리 하는 클래스 JwtTokenProvider, @Value : yml JWT 시크릿 값을 읽어옴, "${jwt.secret}" 이부분은 yml 키에 해당하는 부분
	//그리고 secretKey 변수에 저장, 그리고 가져온 시크릿 키로 토큰을 생성할때 서명하는데에 사용
	public String generateToken(Authentication authentication) { // 로그인 서비스 던질때 같이~
		String email = null;
		if(authentication.getPrincipal().getClass() == principalUser.class) {
			principalUser principalUser = (principalUser) authentication.getPrincipal();
			email = principalUser.getEmail();
		}
		if(authentication.getAuthorities() == null) {
			throw new RuntimeException("등록된 권한이 없습니다.");
		}
		//사용자의 이름과 권한 정보를 기반으로 JWT를 생성
		StringBuilder builder = new StringBuilder(); // 문자열을 효율적으로 조작할 수 있다, 기존 들어왔던 문자와 그뒤 들어올 문자를 추가해 줄때 좋은 클래스이다
		authentication.getAuthorities().forEach(authority -> {
			builder.append(authority.getAuthority() + ",");
		});// 사용자 권한 정보를 가져오는 forEach 문
		builder.delete(builder.length() - 1, builder.length());
		// 마지막 권한 뒤에 추가된 쉼표를 삭제
		String authorities = builder.toString();  // 간단하게 말하면 하나의 String 문자로 변형

		// StringBuilder 객체에 저장된 문자열을 String으로 변환하여 저장
		Date tokenExpiresDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24)); //현재시간 + 하루
		
		return Jwts.builder()
				.setSubject("AuthRegister") //토큰의 제목
				.claim("email", email)
				.claim("auth", authorities)	//auth
				.setExpiration(tokenExpiresDate) //토큰의 만료시간
				.signWith(key, SignatureAlgorithm.HS256)  //토큰 암호화
				.compact();
		
		
	}// JwtRespDto에 빌더를 사용해 변수에 넣어줌
	public boolean validateToken(String token) {
		//JWT 토큰을 유효성 검사하는 메서드
		try {
			Jwts.parserBuilder() //주어진 토큰을 파싱하고 JWT서명을 검증
			.setSigningKey(key) //서명키로 서명확인
			.build()
			.parseClaimsJws(token);
			return true; // 토큰이 유효하면(서명이 올바르고, 만료되지 않았고, 형식이 올바르다면) true를 반환
		} catch (SecurityException | MalformedJwtException e) { // 서명이 잘못됨 | 잘못된 형식의 JWT
//			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) { //JWT가 만료된 경우
//			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) { //지원되지 않는 JWT 형식
//			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) { //잘못된 인수가 전달된 경우
//			log.info("IllegalArgument JWT Token", e);
		} catch (Exception e) {
//			log.info("JWT Token Error", e);
		}
			return false;
	}
	public String getToken(String token) { // 이 코드는 주어진 토큰에서 실제 JWT 문자열을 추출하는 메서드
		String type = "Bearer"; //이것은 일반적으로 JWT가 HTTP 요청 헤더에서 전달될 때 사용되는 인증 방식의 일부
		if(StringUtils.hasText(token) && token.startsWith(type)) { //null인지 아닌지, 그 토큰이 "Bearer"로 시작하는지 확인합니다.
			return token.substring(type.length() +1); 
			//만약 토큰이 존재하고 "Bearer"로 시작한다면, "Bearer " 부분을 제외한 나머지 문자열을 반환합니다. 
			//이렇게 하면 실제 JWT 문자열만 추출됩니다.
		} 
		return null;
	}
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key) //파서에 서명 키를 설정합니다. 이는 JWT를 검증하는 데 사용됩니다.
				.build() 
				.parseClaimsJws(token) //토큰을 파싱하고 검증합니다. 이것은 서명이 올바르고 유효한 JWT인지 확인합니다.
				.getBody();
		///파싱된 JWT의 본문을 가져옵니다. 이 본문은 클레임(claims)을 포함하고 있으며, 이는 JWT에 포함된 정보를 나타냅니다.
	}
	public Authentication getAuthentication(String accessToken) { // 주어진 토큰을 사용하여 사용자의 인증정보를 가져오는 메서드
		Authentication authentication = null;
		Claims claims = getClaims(accessToken);
		
		String email = claims.get("email").toString();
		User userEntity =  userRepository.findUserByEmail(email);		
		
		principalUser principalUser = userEntity.toPrincipal();
		
		authentication = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
		
		return authentication;
	}
}
