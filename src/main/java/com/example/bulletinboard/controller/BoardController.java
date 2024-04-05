package com.example.bulletinboard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bulletinboard.dto.board.WriteReqDto;
import com.example.bulletinboard.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {	
	
	private final BoardService boardService;
	
	@PostMapping("/write")
	public ResponseEntity<?> write(@RequestBody WriteReqDto writeReqDto){
		
		return ResponseEntity.ok().body(boardService.write(writeReqDto));
	}
}
