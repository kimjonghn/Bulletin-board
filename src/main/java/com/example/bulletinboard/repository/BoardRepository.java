package com.example.bulletinboard.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.bulletinboard.entity.Board;
import com.example.bulletinboard.entity.Comment;

@Mapper
public interface BoardRepository {
	
	public int write(Map<String, Object> map);
	public List<Board> board();
	public Board viewPost(int boardId);
	public int boardDelete(int boardId);
	public int modify(Map<String, Object> map );
	public int comment(Map<String , Object>mmap );
	public List<Comment> getComment(int boardId);
}
