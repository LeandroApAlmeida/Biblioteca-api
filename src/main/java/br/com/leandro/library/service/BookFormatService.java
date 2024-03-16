package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.BookFormat;
import br.com.leandro.library.repository.BookFormatRepository;

@Service
public class BookFormatService {
	
	
	@Autowired
	private BookFormatRepository bookFormatRepository;
	
	
	public List<BookFormat> getAllBookFormats(){
		return bookFormatRepository.findAll();
	}
	
	
	public BookFormat getBookFormat(int id) {
		Optional<BookFormat> bookFormatO = bookFormatRepository.findById(id);
		if (bookFormatO.isEmpty()) throw new ResourceNotFoundException(
			String.valueOf(id),
			"Book format not found."
		);
		return bookFormatO.get();
	}
	

}
