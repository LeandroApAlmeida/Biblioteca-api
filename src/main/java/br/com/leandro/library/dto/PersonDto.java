package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PersonDto(
	
	UUID id,
	
	String name,
	
	String description,
	
	LocalDateTime registrationDate,

	LocalDateTime lastUpdateDate
	
) {
}