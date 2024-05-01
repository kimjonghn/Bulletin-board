package com.example.bulletinboard.entity;

import com.example.bulletinboard.dto.board.BoardResDto;
import com.example.bulletinboard.dto.board.ViewPostResDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {
	private int boardId;
	private String title;
	private String content;
	private int userId;
	private String name;
	private String time;

	public BoardResDto toDto() {
		return BoardResDto.builder()
				.boardId(boardId)
				.title(title)
				.content(content)
				.userId(userId)
				.name(name)
				.time(time)
				.build();
	}
	public ViewPostResDto toGetDto() {
		return ViewPostResDto.builder()
				.boardId(boardId)
				.title(title)
				.content(content)
				.userId(userId)
				.name(name)
				.build();
	}
}
	
