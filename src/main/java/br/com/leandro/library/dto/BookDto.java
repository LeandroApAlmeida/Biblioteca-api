package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record BookDto(
	
	UUID id,
	
	int idFormat,

    String title,

    String subtitle,

    String author,

    String publisher,

    String isbn,

    int edition,

    int volume,

    int releaseYear,
    
    int numberOfPages,

    String summary,
    
    LocalDateTime acquisitionDate,

    LocalDateTime registrationDate,

    LocalDateTime lastUpdateDate
		
) {
	
}