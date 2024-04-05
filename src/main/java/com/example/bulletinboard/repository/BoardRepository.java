package com.example.bulletinboard.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardRepository {
	
	public int write(Map<String, Object> map);
}
