package br.com.leandro.library.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LoginResponse {
	
	private String id;
	
	private String token;
	
	private LocalDateTime time;

}
