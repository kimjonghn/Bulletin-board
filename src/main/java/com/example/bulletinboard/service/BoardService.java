package com.example.bulletinboard.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

import io.jsonwebtoken.io.IOException;
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
		List<MultipartFile> images = writeReqDto.getImages();
		
		if(title != null && !title.isEmpty() && title.length() >= 2) {
			if(content != null && !content.isEmpty() && content.length() >= 2) {
				
				LocalDateTime localDateTime = LocalDateTime.now();
				String todayDateTime = String.format("%04d-%02d-%02d", 
														localDateTime.getYear(),
														localDateTime.getMonthValue(),
				                                        localDateTime.getDayOfMonth());
				
				Map<String, Object> map = new HashMap<>();
				
				map.put("title" , writeReqDto.getTitle());
				map.put("content" , writeReqDto.getContent());
				map.put("userId", principalUser.getUserId());
				map.put("name" , principalUser.getName());
				map.put("time", todayDateTime);
				
				 if (images != null && !images.isEmpty()) {
				        List<String> imageFileNames = new ArrayList<>();
				        for (MultipartFile image : images) {
				            try {
				                String originalFilename = image.getOriginalFilename();
				                if (originalFilename != null) {
				                	 String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
				                    String filePath = "C:\\junil\\jonghwan\\workspace\\bulletin_Board\\Bulletin-Board_pront\\public\\images\\" + fileName;
				                    File dest = new File(filePath);
				                    if (!dest.getParentFile().exists()) {
				                        dest.getParentFile().mkdirs();
				                    }
				                    image.transferTo(dest);
				                    imageFileNames.add(fileName);  // 저장한 파일 이름을 리스트에 추가
				                }
						}catch (Exception e) {
							e.printStackTrace();
							return 0;
						}
					}   
				    map.put("imageFileNames", String.join(",", imageFileNames));
				}else {
					map.put("imageFileNames",  "");					
				}
				return boardRepository.write(map);
			}
			return 0;
				
			}else {
				return 0;
			}}
	

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
	public int modify(int boardId, WriteReqDto writeReqDto) {
	    String title = writeReqDto.getTitle();
	    String content = writeReqDto.getContent();
	    List<MultipartFile> images = writeReqDto.getImages();
	    List<String> existingImageFileNames = writeReqDto.getExistingImages();
	    
	    if (title != null && !title.isEmpty() && title.length() >= 2) {
	        if (content != null && !content.isEmpty() && content.length() >= 2) {
	            LocalDateTime localDateTime = LocalDateTime.now();
	            String todayDateTime = String.format("%04d-%02d-%02d", 
	                                                 localDateTime.getYear(),
	                                                 localDateTime.getMonthValue(),
	                                                 localDateTime.getDayOfMonth());
	            
	            Map<String, Object> map = new HashMap<>();
	            map.put("boardId", boardId);
	            map.put("title", title);
	            map.put("content", content);
	            map.put("time", todayDateTime);
	            
	            // 기존 이미지 파일 이름 리스트가 null이면 빈 리스트로 초기화
	            List<String> imageFileNames = existingImageFileNames != null ? new ArrayList<>(existingImageFileNames) : new ArrayList<>();
	            if (images != null && !images.isEmpty()) {
	                for (MultipartFile image : images) {
	                    try {
	                        String originalFilename = image.getOriginalFilename();
	                        if (originalFilename != null) {
	                            String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
	                            String filePath = "C:\\junil\\jonghwan\\workspace\\bulletin_Board\\Bulletin-Board_pront\\public\\images\\" + fileName;
	                            File dest = new File(filePath);
	                            if (!dest.getParentFile().exists()) {
	                                dest.getParentFile().mkdirs();
	                            }
	                            image.transferTo(dest);
	                            imageFileNames.add(fileName);  // 저장한 파일 이름을 리스트에 추가
	                        }
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        return 0;
	                    }
	                }
	            }
	            map.put("imageFileNames", String.join(",", imageFileNames));
	            return boardRepository.modify(map);
	        } else {
	            return 0;
	        }
	    } else {
	        return 0;
	    }
	}

	public int comment(int boardId, String accessToken, String comment) {
		String email = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken)).get("email").toString();
		if (comment == null || comment.trim().isEmpty()) {
	        throw new IllegalArgumentException("댓글 내용은 공백이거나 null일 수 없습니다.");
	    }
		int userId = userRepository.findUserByEmail(email).getUserId();
		String userName = userRepository.findUserByEmail(email).getName();
		
		JsonObject jObject = JsonParser.parseString(comment).getAsJsonObject();
		String commentText = jObject.get("comment").getAsString();
		 if (commentText == null || commentText.trim().isEmpty()) {
		        throw new IllegalArgumentException("댓글 내용은 공백이거나 null일 수 없습니다.");
		    }
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
	
	public List<Comment> commentUseCheck(String accessToken, int boardId) {
		String email = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken)).get("email").toString();
		int userId = userRepository.findUserByEmail(email).getUserId();
		
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("boardId", boardId);
		System.out.println(boardRepository.commentUserCheck(map));
		return boardRepository.commentUserCheck(map);
	}
	public int commentDelete(int boardId, int deleteCommentId) {
		Map<String, Object> map = new HashMap<>();
		map.put("boardId", boardId);
		map.put("deleteCommentId", deleteCommentId);
		
		
		
		return boardRepository.commentDelete(map);
	}
}
