package com.example.bulletinboard.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bulletinboard.dto.board.WriteReqDto;
import com.example.bulletinboard.service.BoardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {	
	
	private final BoardService boardService;
	
	@PostMapping("/write")
	public ResponseEntity<?> write(@ModelAttribute WriteReqDto writeReqDto){
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
	@GetMapping("/getcomment/{boardId}")
	public ResponseEntity<?> getComment(@PathVariable int boardId){
		
		
		return ResponseEntity.ok().body(boardService.getComment(boardId));
	}
	@GetMapping("/comment/usercheck/{boardId}")
	public ResponseEntity<?> commentUserCheck(@RequestHeader(value="Authorization")String accessToken, @PathVariable int boardId){
		
		
		return ResponseEntity.ok().body(boardService.commentUseCheck(accessToken, boardId));
	}
	
	@DeleteMapping("commentdelete/{boardId}/{deleteCommentId}")
	public ResponseEntity<?> commentDelete(@PathVariable int boardId, @PathVariable int deleteCommentId){
		
		return ResponseEntity.ok().body(boardService.commentDelete(boardId, deleteCommentId));
}
	
}
