package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.DiscardedBookDto;
import br.com.leandro.library.exception.RegistrationException;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.DiscardedBook;
import br.com.leandro.library.repository.BookRepository;
import br.com.leandro.library.repository.DiscardedBookRepository;

@Service
public class DiscardedBookService {
	
	
	@Autowired
	private DiscardedBookRepository discardedBookRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	public DiscardedBook saveDiscardedBook(DiscardedBookDto discardedBookDto) {
		Optional<DiscardedBook> discardedBookO = discardedBookRepository.findById(
			discardedBookDto.idBook()
		);
		if (discardedBookO.isPresent()) throw new RegistrationException(
			discardedBookDto.idBook().toString(),
			"The book has already been discarded."
		);
		Optional<Book> bookO = bookRepository.findById(discardedBookDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			discardedBookDto.idBook().toString(),
			"Book not found."
		);
		DiscardedBook discardedBook = new DiscardedBook(
			discardedBookDto.idBook(),
			bookO.get(),
			discardedBookDto.reason(),
			discardedBookDto.date(),
			discardedBookDto.lastUpdateDate()
		);
		return discardedBookRepository.save(discardedBook);
	}
	
	
	public DiscardedBook updateDiscardedBook(UUID id, DiscardedBookDto discardedBookDto) {
		Optional<DiscardedBook> discardedBookO = discardedBookRepository.findById(id);
		if (discardedBookO.isEmpty()) throw new ResourceNotFoundException(
			discardedBookDto.idBook().toString(),
			"Discarded book not found."
		);
		DiscardedBook discardedBook = discardedBookO.get();
		discardedBook.setDate(discardedBookDto.date());
		discardedBook.setReason(discardedBookDto.reason());
		discardedBook.setLastUpdateDate(discardedBookDto.lastUpdateDate());
		return discardedBookRepository.save(discardedBook);
	}
	
	
	public void deleteDiscardedBook(UUID id) {
		Optional<DiscardedBook> discardedBookO = discardedBookRepository.findById(id);
		if (discardedBookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Discarded book not found."
		);
		discardedBookRepository.delete(discardedBookO.get());
	}
	
	
	public List<DiscardedBook> getAllDiscardedBooks() {
		return discardedBookRepository.findAll();
	}
	
	
	public DiscardedBook getDiscardedBook(UUID id) {
		Optional<DiscardedBook> discardedBookO = discardedBookRepository.findById(id);
		if (discardedBookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Discarded book not found."
		);
		return discardedBookO.get();
	}
	
	
}