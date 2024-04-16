package com.example.bulletinboard.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.bulletinboard.entity.Board;

@Mapper
public interface BoardRepository {
	
	public int write(Map<String, Object> map);
	public List<Board> board();
	public Board viewPost(int boardId);
	public int boardDelete(int boardId);
	public int modify(Map<String, Object> map );
}
