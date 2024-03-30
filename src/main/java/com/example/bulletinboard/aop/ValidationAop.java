package com.example.bulletinboard.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.example.bulletinboard.exception.CustomException;


@Component
@Aspect
public class ValidationAop {
	// 어디에서든 적용할 수 있는 지점을 정의한다 , ValidAspect 어노테이션에 메서드 적용시킴
	@Pointcut("@annotation(com.example.bulletinboard.aop.annotation.ValidAspect)") 
	private void pointCut() {
		
	}
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable { //joinPoint (dto, BindingResult) 들어있음
		// 컨트롤러 메서드에서 HTTP 요청 파라미터를 처리하는 경우, args 배열에는 해당 요청의 파라미터 값들이 포함됨
		Object[] args = joinPoint.getArgs(); // [0]Dto , [1]bindingresult가 들어있음
		BindingResult bindingResult = null;
		
		for(Object arg : args) {//1번째는 dto 2번째는 bindingResult(오류 정보가 담겨있음) 
			if(arg.getClass() == BeanPropertyBindingResult.class) { // bindingResult == BeanPropertyBindingResult 같으므로 실행됨
				bindingResult = (BeanPropertyBindingResult) arg;  //다운캐스팅 , bindingresult 타입인지를 확인하고 맞다면 bindingresult변수에 할당 
			}
		}
		
		if(bindingResult.hasErrors()) { //오류가 있는지 없는지 확인
			Map<String, String> errorMap = new HashMap<>();
			
			bindingResult.getFieldErrors().forEach(error -> {
				errorMap.put(error.getField(), error.getDefaultMessage()); //key : value (오류메세지를 만들어준다)
			});
			throw new CustomException("Validation Failed", errorMap); // 오류가 있을경우 실행됨
		}
		
		return joinPoint.proceed(); //proceed 실행되면 authenticationService.checkDuplicatedEmail(signupReqDto.getEmail()); 실행
	}
}

