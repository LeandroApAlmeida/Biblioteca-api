package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record LoanDto(

	UUID id,

	UUID idPerson,

	@JsonFormat(pattern = JSonFormat.DATE_FORMAT)
	LocalDateTime date,

	String notes,
	
	@JsonFormat(pattern = JSonFormat.DATE_FORMAT)
	LocalDateTime lastUpdateDate
	
) {
}