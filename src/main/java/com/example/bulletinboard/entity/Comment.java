package com.example.bulletinboard.entity;

import com.example.bulletinboard.dto.board.CommentResDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment {
	private int commentId;
	private String commentContent;
	private int userId;
	private String name;
	private int boardId;
	
	public CommentResDto toDto(){
		return CommentResDto.builder()
				.commentId(commentId)
				.commentContent(commentContent)
				.userId(userId)
				.name(name)
				.boardId(boardId)
				.build();
	}
}
