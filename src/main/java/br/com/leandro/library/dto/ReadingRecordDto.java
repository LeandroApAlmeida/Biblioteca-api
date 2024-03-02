package br.com.leandro.library.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReadingRecordDto(

    UUID id,

    UUID idBook,

    @JsonFormat(pattern = JSonFormat.DATE_FORMAT)
    LocalDateTime beginDate,

    @JsonFormat(pattern = JSonFormat.DATE_FORMAT)
    LocalDateTime endDate,

    String notes,

    @JsonFormat(pattern = JSonFormat.DATE_FORMAT)
    LocalDateTime registrationDate,

    @JsonFormat(pattern = JSonFormat.DATE_FORMAT)
    LocalDateTime lastUpdateDate,

    boolean readingCompleted,

    boolean isDeleted
    
) {
}