package com.example.bulletinboard.dto.board;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WriteReqDto {
	private String title;
	private String content;
}
