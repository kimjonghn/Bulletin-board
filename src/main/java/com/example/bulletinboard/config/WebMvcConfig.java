package com.example.bulletinboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //이것이 없으면 cors를 구성할수 없다 @crossOrigin 을 달아 줘야함!
				.allowedMethods("*") // get, post, update, delete 를 할수있게 도와줌
				.allowedOrigins("*"); //CORS 정책에서 허용할 출처(origin)를 지정하는 부분입니다. 여기서 *는 모든 출처를 허용한다는 의미입니
		
	}
	
	
}
