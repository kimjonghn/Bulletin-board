package com.example.bulletinboard.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.bulletinboard.entity.Authority;
import com.example.bulletinboard.entity.User;

@Mapper
public interface UserRepository {
	
	public User findUserByEmail(String email);
	public int saveUser(User user);
	public int saveAuthority(Authority authority);
	public int userDelete(User user);
	public int boardUserId(String boardId);
	
}
