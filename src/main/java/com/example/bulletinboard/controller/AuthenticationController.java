 package com.example.bulletinboard.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bulletinboard.aop.annotation.ValidAspect;
import com.example.bulletinboard.dto.auth.LoginReqDto;
import com.example.bulletinboard.dto.auth.SignupReqDto;
import com.example.bulletinboard.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@ValidAspect
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto , BindingResult bindingResult){
		authenticationService.checkDuplicatedEmail(signupReqDto.getEmail());
		authenticationService.signup(signupReqDto);
		return ResponseEntity.ok().body(true);
	}
	
	@ValidAspect
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto , BindingResult bindingResult){
		return ResponseEntity.ok(authenticationService.signin(loginReqDto));
	}
	
	@GetMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestHeader(value="Authorization")String accessToken){

		return ResponseEntity.ok().body(authenticationService.authenticate(accessToken));
	}
}
