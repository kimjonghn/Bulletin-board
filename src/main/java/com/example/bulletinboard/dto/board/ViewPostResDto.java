package com.example.bulletinboard.dto.board;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ViewPostResDto {
	private int boardId;
	private String title;
	private String content;
	private int userId;
	private String name;
}
