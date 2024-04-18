package com.example.bulletinboard.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
	private int commentId;
	private String commentContent;
	private int userId;
	private String name;
	private int boardId;
}
