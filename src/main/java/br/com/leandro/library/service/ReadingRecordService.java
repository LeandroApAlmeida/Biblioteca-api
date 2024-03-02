package br.com.leandro.library.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.ReadingRecordDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.ReadingRecord;
import br.com.leandro.library.repository.BookRepository;
import br.com.leandro.library.repository.ReadingRecordRepository;

@Service
public class ReadingRecordService {
	
	
	@Autowired
	private ReadingRecordRepository readingRecordRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	public ReadingRecord saveReadingRecord(ReadingRecordDto readingRecordDto) {
		Optional<Book> bookO = bookRepository.findById(readingRecordDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			readingRecordDto.id().toString(),
			"Book not found."
		);
		ReadingRecord readingRecord = new ReadingRecord(
			readingRecordDto.id(), 
			bookO.get(),
			readingRecordDto.beginDate(),
			readingRecordDto.endDate(),
			readingRecordDto.notes(),
			readingRecordDto.registrationDate(),
			readingRecordDto.lastUpdateDate(),
			false,
			false
		);	
		return readingRecordRepository.save(readingRecord);
	}
	
	
	public ReadingRecord updateReadingRecord(UUID id, ReadingRecordDto readingRecordDto) {
		Optional<ReadingRecord> readingRecordO = readingRecordRepository.findById(id);
		if (readingRecordO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Reading record not found."
		);
		Optional<Book> bookO = bookRepository.findById(readingRecordDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			readingRecordDto.id().toString(),
			"Book not found."
		);
		ReadingRecord readingRecord = readingRecordO.get();
		readingRecord.setBook(bookO.get());
		readingRecord.setBeginDate(readingRecordDto.beginDate());
		readingRecord.setEndDate(readingRecordDto.endDate());
		readingRecord.setNotes(readingRecordDto.notes());
		readingRecord.setRegistrationDate(readingRecordDto.registrationDate());
		readingRecord.setLastUpdateDate(readingRecordDto.lastUpdateDate());
		return readingRecordRepository.save(readingRecord);
	}
	

}