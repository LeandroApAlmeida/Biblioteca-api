package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record LoanDto(

	UUID id,

	UUID idPerson,

	LocalDateTime date,

	String notes,
	
	LocalDateTime lastUpdateDate
	
) {
	
}