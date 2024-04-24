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

@Slf4j 
@Component 
public class JwtTokenProvider {
	
	@Autowired
	private UserRepository userRepository;
	private final Key key;


	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		
	}
	public String generateToken(Authentication authentication) { 
		String email = null;
		if(authentication.getPrincipal().getClass() == principalUser.class) {
			principalUser principalUser = (principalUser) authentication.getPrincipal();
			email = principalUser.getEmail();
		}
		if(authentication.getAuthorities() == null) {
			throw new RuntimeException("등록된 권한이 없습니다.");
		}
		
		StringBuilder builder = new StringBuilder();
		authentication.getAuthorities().forEach(authority -> {
			builder.append(authority.getAuthority() + ",");
		});
		builder.delete(builder.length() - 1, builder.length());
	
		String authorities = builder.toString(); 

	
		Date tokenExpiresDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24)); 
		
		return Jwts.builder()
				.setSubject("AuthRegister")
				.claim("email", email)
				.claim("auth", authorities)	
				.setExpiration(tokenExpiresDate) 
				.signWith(key, SignatureAlgorithm.HS256)  
				.compact();
		
		
	}
	public boolean validateToken(String token) {
		
		try {
			Jwts.parserBuilder() 
			.setSigningKey(key) 
			.build()
			.parseClaimsJws(token);
			return true; 
		} catch (SecurityException | MalformedJwtException e) {
//			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) { 
//			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) { 
//			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) { 
//			log.info("IllegalArgument JWT Token", e);
		} catch (Exception e) {
//			log.info("JWT Token Error", e);
		}
			return false;
	}
	public String getToken(String token) { 
		String type = "Bearer"; 
		if(StringUtils.hasText(token) && token.startsWith(type)) {
			return token.substring(type.length() +1); 
		} 
		return null;
	}
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key) 
				.build() 
				.parseClaimsJws(token) 
				.getBody();
	}
	public Authentication getAuthentication(String accessToken) { 
		Authentication authentication = null;
		Claims claims = getClaims(accessToken);
		
		String email = claims.get("email").toString();
		User userEntity =  userRepository.findUserByEmail(email);		
		
		principalUser principalUser = userEntity.toPrincipal();
		
		authentication = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
		
		return authentication;
	}
}
