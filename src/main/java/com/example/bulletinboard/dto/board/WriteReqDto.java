package com.example.bulletinboard.dto.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WriteReqDto {
	private String title;
	private String content;
	private List<MultipartFile> images;
	private List<String> existingImages;
}
