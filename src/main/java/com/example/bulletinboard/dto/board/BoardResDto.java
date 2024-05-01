package com.example.bulletinboard.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResDto {
	private int boardId;
	private String title;
	private String content;
	private int userId;
	private String name;
	private String time;
}
