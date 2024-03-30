package com.example.bulletinboard.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ValidAspect {
	// 내가 임의로 어노테이션을 만듬
	// 이 어노테이션이 달려있는 경우 ValidationAop 이것이 실행됨
}
