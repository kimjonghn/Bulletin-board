package com.example.bulletinboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.bulletinboard.dto.board.BoardResDto;
import com.example.bulletinboard.dto.board.ViewPostResDto;
import com.example.bulletinboard.dto.board.WriteReqDto;
import com.example.bulletinboard.repository.BoardRepository;
import com.example.bulletinboard.security.principalUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public int write(WriteReqDto writeReqDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  //현재 로그인한 사용자 정보
		principalUser principalUser = (principalUser) authentication.getPrincipal(); //현재 인증된 사용자
		
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
}
