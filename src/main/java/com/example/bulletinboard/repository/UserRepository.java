package com.example.bulletinboard.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.bulletinboard.entity.Authority;
import com.example.bulletinboard.entity.User;

@Mapper
public interface UserRepository {
	
	public User findUserByEmail(String email);
	public User findUserByPhone(String phone);
	public int saveUser(User user);
	public int saveAuthority(Authority authority);
	public int userDelete(User user);
	public int boardUserId(String boardId);
	public User findEmail(String phone);
	public int findPassword(Map<String, Object> map);
	public void passwordChange(User user);
	
}
