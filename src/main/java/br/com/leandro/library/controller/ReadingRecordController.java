package br.com.leandro.library.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.library.dto.ReadingRecordDto;
import br.com.leandro.library.model.ReadingRecord;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.ReadingRecordService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/readingrecords")
public class ReadingRecordController {

	
	@Autowired
	private ReadingRecordService readingRecordService;
	
	
	@PostMapping
	public ResponseEntity<Response> saveReadingRecord(
		@RequestBody @Valid ReadingRecordDto readingRecordDto
	) {
		ReadingRecord readingRecord = readingRecordService.saveReadingRecord(
			readingRecordDto
		);
		Response resp = new Response();
		resp.setId(readingRecord.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Reading record successfully registered.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
}
