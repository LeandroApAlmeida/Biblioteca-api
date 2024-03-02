package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.DonatedBookDto;
import br.com.leandro.library.exception.RegistrationException;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.DonatedBook;
import br.com.leandro.library.model.Person;
import br.com.leandro.library.repository.BookRepository;
import br.com.leandro.library.repository.DonatedBookRepository;
import br.com.leandro.library.repository.PersonRepository;

@Service
public class DonatedBookService {
	
	
	@Autowired
	private DonatedBookRepository donatedBookRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	
	public DonatedBook saveDonatedBook(DonatedBookDto donatedBookDto) {
		Optional<DonatedBook> donatedBookO = donatedBookRepository.findById(donatedBookDto.idBook());
		if (donatedBookO.isPresent()) throw new RegistrationException(
			donatedBookDto.idBook().toString(),
			"The book has already been donated."
		);
		Optional<Book> bookO = bookRepository.findById(donatedBookDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			donatedBookDto.idBook().toString(),
			"Book not found."
		);
		Optional<Person> personO = personRepository.findById(donatedBookDto.idPerson());
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			donatedBookDto.idBook().toString(),
			"Person not found."
		);
		DonatedBook donatedBook = new DonatedBook(
			donatedBookDto.idBook(),
			bookO.get(),
			personO.get(),
			donatedBookDto.notes(),
			donatedBookDto.date(),
			donatedBookDto.lastUpdateDate()
		);
		return donatedBookRepository.save(donatedBook);
	}
	
	
	public DonatedBook updateDonatedBook(UUID id, DonatedBookDto donatedBookDto) {
		Optional<DonatedBook> donatedBookO = donatedBookRepository.findById(id);
		if (donatedBookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Donated book not found."
		);
		Optional<Person> personO = personRepository.findById(donatedBookDto.idPerson());
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Person not found."
		);
		DonatedBook donatedBook = donatedBookO.get();
		donatedBook.setPerson(personO.get());
		donatedBook.setDate(donatedBookDto.date());
		donatedBook.setNotes(donatedBookDto.notes());
		donatedBook.setLastUpdateDate(donatedBookDto.lastUpdateDate());
		return donatedBookRepository.save(donatedBook);
	}
	
	
	public void deleteDonatedBook(UUID id) {
		Optional<DonatedBook> donatedBookO = donatedBookRepository.findById(id);
		if (donatedBookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Donated book not found."
		);
		donatedBookRepository.delete(donatedBookO.get());
	}
	
	
	public List<DonatedBook> getAllDonatedBooks() {
		return donatedBookRepository.findAll();
	}
	
	
	public DonatedBook getDonatedBook(UUID id) {
		Optional<DonatedBook> donatedBookO = donatedBookRepository.findById(id);
		if (donatedBookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Donated book not found."
		);
		return donatedBookO.get();
	}
	
	
}