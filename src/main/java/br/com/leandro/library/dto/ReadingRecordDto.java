package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReadingRecordDto(

    UUID id,

    UUID idBook,

    LocalDateTime beginDate,

    LocalDateTime endDate,

    String notes,
    
    LocalDateTime registrationDate,

    LocalDateTime lastUpdateDate,

    boolean readingCompleted,

    boolean isDeleted
    
) {
}