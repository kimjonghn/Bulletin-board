 package com.example.bulletinboard.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bulletinboard.aop.annotation.ValidAspect;
import com.example.bulletinboard.dto.auth.LoginReqDto;
import com.example.bulletinboard.dto.auth.PasswordUpdateDto;
import com.example.bulletinboard.dto.auth.SignupReqDto;
import com.example.bulletinboard.entity.User;
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
		authenticationService.checkDuplicatedPhone(signupReqDto.getPhone());
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
	
	@GetMapping("/findemail")
	public ResponseEntity<?> findEmail(@RequestParam String phone){
		
		return ResponseEntity.ok().body(authenticationService.findEmail(phone));
	}
	
	@GetMapping("/findpassword")
	public ResponseEntity<?> findEmail(@RequestParam Map<String, Object> map){
		
		return ResponseEntity.ok().body(authenticationService.findPassword(map));
	}
	@PutMapping("/updatepassword/{userid}")
	public ResponseEntity<?> passwordchange(@PathVariable int userid ,@RequestBody PasswordUpdateDto passwordUpdateDto){

		authenticationService.passwordChange(userid, passwordUpdateDto);
		return ResponseEntity.ok().body(null);
	}
	
	@GetMapping("/userInfo")
	public ResponseEntity<?> userInfo(@RequestHeader(value="Authorization")String accessToken){
		
		return ResponseEntity.ok().body(authenticationService.userInfo(accessToken));
	}
	@DeleteMapping("/delete")
	public ResponseEntity<?> userDelete(@RequestBody User user){
		
		return ResponseEntity.ok().body(authenticationService.userDelete(user));
	}
	@GetMapping("/userCheck")
	public ResponseEntity<?> userCheck(
			@RequestHeader(value = "Authorization")String accessToken,
			@RequestParam(value = "boardId") String boardId){
		;
		return ResponseEntity.ok().body(authenticationService.usercheck(accessToken, boardId));
	}
}
