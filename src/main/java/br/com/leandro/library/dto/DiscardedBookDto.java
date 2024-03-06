package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record DiscardedBookDto(
		
	UUID idBook,

	String reason,
	
	LocalDateTime date,

	LocalDateTime lastUpdateDate
		
) {
}