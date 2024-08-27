package com.example.bulletinboard.dto.auth;

import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.bulletinboard.entity.User;

import lombok.Data;

@Data
public class PasswordUpdateDto {
	
	private int userId;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", 
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8 ~ 16자로 작성하세요.")
    private String password;
    
    private String checkPassword;
    

}
