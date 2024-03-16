package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record LoanItemDto(
	
	UUID idloan,
	
	UUID idBook,
	
	boolean returned,
	
	LocalDateTime returnDate,

	String notes,

	LocalDateTime lastUpdateDate
	
) {
	
}