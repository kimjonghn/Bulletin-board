package com.example.bulletinboard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	@GetMapping("")
	public ResponseEntity<?> board(){
		return ResponseEntity.ok().body(boardService.board());
	}
	@GetMapping("/view/{boardId}")
	public ResponseEntity<?> viewPost(@PathVariable int boardId){
		
		return ResponseEntity.ok().body(boardService.viewPost(boardId));
	}
	@DeleteMapping("/delete/{boardId}")
		public ResponseEntity<?> boardDelete(@PathVariable int boardId){
			boardService.boardDelete(boardId);
			return ResponseEntity.ok().body(boardService.boardDelete(boardId));
	}
	@PostMapping("/modify/{boardId}")
	public ResponseEntity<?> modify(@PathVariable int boardId, @RequestBody WriteReqDto writeReqDto){
//		boardService.modify(boardId, writeReqDto);
		return ResponseEntity.ok().body(boardService.modify(boardId, writeReqDto));
	}
	@PostMapping("/comment/{boardId}")
	public ResponseEntity<?> comment(@RequestHeader(value="Authorization")String accessToken,
				@PathVariable int boardId, @RequestBody String comment ){
		
		return ResponseEntity.ok().body(boardService.comment(boardId, accessToken, comment));
	}
	@GetMapping("/getComment/{boardId}")
	public ResponseEntity<?> getComment(@PathVariable int boardId){
		
		
		return ResponseEntity.ok().body(boardService.getComment(boardId));
	}
}
