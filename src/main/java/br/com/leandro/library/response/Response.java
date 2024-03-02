package br.com.leandro.library.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Response {
	
	private String id;
	
	private String status;
	
	private String message;
	
	private LocalDateTime time;

}
