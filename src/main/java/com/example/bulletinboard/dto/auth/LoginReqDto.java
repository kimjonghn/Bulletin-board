package com.example.bulletinboard.dto.auth;

import lombok.Data;

@Data
public class LoginReqDto {
	
//	@Email
//	@NotBlank(message = "이메일을 입력하세요")
	private String email;
	
//	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", //정규식(시작^ 끝&) (//d == 0-9)=숫자를 포함했는지 {8,16} = 8자이상 16자이하
//			message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8 ~ 16자로 작성") 
	private String password;
	
	}
