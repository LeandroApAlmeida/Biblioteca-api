package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record DonatedBookDto(
		
	UUID idBook,

	UUID idPerson,
	
	String notes,
	
	LocalDateTime date,

	LocalDateTime lastUpdateDate
	    
) {
	
}