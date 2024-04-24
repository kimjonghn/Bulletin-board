package com.example.bulletinboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.bulletinboard.dto.board.BoardResDto;
import com.example.bulletinboard.dto.board.CommentResDto;
import com.example.bulletinboard.dto.board.ViewPostResDto;
import com.example.bulletinboard.dto.board.WriteReqDto;
import com.example.bulletinboard.entity.Comment;
import com.example.bulletinboard.repository.BoardRepository;
import com.example.bulletinboard.repository.UserRepository;
import com.example.bulletinboard.security.JwtTokenProvider;
import com.example.bulletinboard.security.principalUser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	
	public int write(WriteReqDto writeReqDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
		principalUser principalUser = (principalUser) authentication.getPrincipal(); 
		
		String title = writeReqDto.getTitle();
		String content = writeReqDto.getContent();
		
		if(title != null && !title.isEmpty() && title.length() >= 2) {
			
			if(content != null && !content.isEmpty() && content.length() >= 2) {
				
				Map<String, Object> map = new HashMap<>();
				
				map.put("title" , writeReqDto.getTitle());
				map.put("content" , writeReqDto.getContent());
				map.put("userId", principalUser.getUserId());
				map.put("name" , principalUser.getName());
				
				return boardRepository.write(map);
			}
			return 0;
				
			}else {
				
				return 0;
			}
	}
	
	public List<BoardResDto> board(){
		
		List<BoardResDto> responseList = new ArrayList<>();
		
		boardRepository.board().forEach(board -> {
			responseList.add(board.toDto());
		});
		
		return responseList;
	}
	public ViewPostResDto viewPost(int boardId){
		return boardRepository.viewPost(boardId).toGetDto();
	}
	
	public int boardDelete(int boardId) {
		return boardRepository.boardDelete(boardId);
	}
	
	public int modify(int boardId , WriteReqDto writeReqDto) {
		Map<String, Object> map = new HashMap<>();
		map.put("boardId", boardId);
		map.put("title", writeReqDto.getTitle());
		map.put("content", writeReqDto.getContent());
		
		return boardRepository.modify(map);
	}
	public int comment(int boardId, String accessToken, String comment) {
		String email = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken)).get("email").toString();
		int userId = userRepository.findUserByEmail(email).getUserId();
		String userName = userRepository.findUserByEmail(email).getName();
		
		JsonObject jObject = JsonParser.parseString(comment).getAsJsonObject();
		String commentText = jObject.get("comment").getAsString();
		
		Map<String, Object> map = new HashMap<>();
		map.put("comment", commentText);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("boardId", boardId);
		
		return boardRepository.comment(map);
	}
	public List<CommentResDto> getComment(int boardId) {
		
		List<CommentResDto> commentList = new ArrayList<>();
		boardRepository.getComment(boardId).forEach(comment -> {
			commentList.add(comment.toDto());
		});
		return commentList;
	}
}
