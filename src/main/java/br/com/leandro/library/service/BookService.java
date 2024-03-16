package br.com.leandro.library.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import br.com.leandro.library.dto.BookDto;
import br.com.leandro.library.exception.RegistrationException;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.BookCover;
import br.com.leandro.library.model.BookFormat;
import br.com.leandro.library.repository.BookCoverRepository;
import br.com.leandro.library.repository.BookRepository;
import br.com.leandro.library.system.ServerPaths;


@Service
public class BookService {

	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookFormatService bookFormatService;
	
	@Autowired
	private BookCoverRepository bookCoverRepository;
	
	@Autowired	
    private ServerPaths serverPaths;
	
	
	private File getCoverFile(String title, String isbn, MultipartFile cover)
	throws IOException {
		String bookTitle = title.replace(" ", "_");
		String fileExt = Files.getFileExtension(cover.getOriginalFilename());
		String fileName = bookTitle.toUpperCase().concat("_").concat(isbn.toUpperCase())
		.concat(".").concat(fileExt);
		return serverPaths.getMediaFile(fileName);
	}
	
	
	private File getCoverFile(String fileName) {
		return serverPaths.getMediaFile(fileName);
	}
	
	
	public byte[] getCover(UUID idCover) {
		try {
			Optional<BookCover> coverO = bookCoverRepository.findById(idCover);
			if (coverO.isPresent()) {
				File file = new File(serverPaths.getMediaDirectory() + coverO.get().getFilePath());
				return Files.toByteArray(file);
			} else {
				throw new Exception("Cover file not found.");
			}
		} catch (Exception ex) {
			throw new ResourceNotFoundException(
				idCover.toString(),
				ex.getMessage()
			);
		}
	}
	
	
	public Book saveBook(BookDto bookDto, MultipartFile cover) {
		File coverFile = null;
		try {
			BookFormat format = bookFormatService.getBookFormat(
				bookDto.idFormat()
			);
			coverFile = getCoverFile(bookDto.title(), bookDto.isbn(), cover);
			if (coverFile.exists()) throw new Exception(
				"The book has already been registered."
			);
			Files.write(cover.getBytes(), coverFile);
			BookCover bookCover = bookCoverRepository.save(
				new BookCover(
					UUID.randomUUID(),
					coverFile.getName()
				)
			);
			Book bookModel = new Book(
				bookDto.id(),
				format,
				bookCover,
				bookDto.title(),
				bookDto.subtitle(),
				bookDto.author(),
				bookDto.publisher(),
				bookDto.isbn(),
				bookDto.edition(),
				bookDto.volume(),
				bookDto.releaseYear(),
				bookDto.numberOfPages(),
				bookDto.summary(),
				bookDto.acquisitionDate(),
				bookDto.registrationDate(),
				bookDto.lastUpdateDate(),
				false
			);
			return bookRepository.save(bookModel);
		} catch (Exception ex) {
			if (coverFile != null) coverFile.delete();
			throw new RegistrationException(
				bookDto.id().toString(),
				ex.getMessage()
			);
		}
	}
	
	
	public Book updateBook(UUID id, BookDto bookDto, MultipartFile cover) {
		try {
			Book book = getBook(id);
			BookFormat format = bookFormatService.getBookFormat(bookDto.idFormat());
			book.setFormat(format);
			book.setTitle(bookDto.title());
			book.setSubtitle(bookDto.subtitle());
			book.setAuthor(bookDto.author());
			book.setPublisher(bookDto.publisher());
			book.setIsbn(bookDto.isbn());
			book.setEdition(bookDto.edition());
			book.setVolume(bookDto.volume());
			book.setReleaseYear(bookDto.releaseYear());
			book.setNumberOfPages(bookDto.numberOfPages());
			if (!cover.isEmpty()) {
				File coverFile = null;
				coverFile = getCoverFile(bookDto.title(), bookDto.isbn(), cover);
				Files.write(cover.getBytes(), coverFile);
				BookCover bookCover = book.getCover();
				File oldFile = getCoverFile(bookCover.getFilePath());
				bookCover.setFilePath(coverFile.getName());
				bookCoverRepository.save(bookCover);
				if (coverFile.equals(oldFile)) {
					oldFile.delete();
				}
			}
			book.setSummary(bookDto.summary());
			book.setAcquisitionDate(bookDto.acquisitionDate());
			book.setRegistrationDate(bookDto.registrationDate());
			book.setLastUpdateDate(bookDto.lastUpdateDate());
			return bookRepository.save(book);
		} catch (Exception ex) {
			throw new RegistrationException(
				bookDto.id().toString(),
				ex.getMessage()
			);
		}
	}
	
	
	public Book deleteBook(UUID idBook) {
		Optional<Book> bookO = bookRepository.findById(idBook);
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			idBook.toString(),
			"Book not found."
		);
		var book = bookO.get();
		book.setDeleted(true);
		bookRepository.save(book);
		return book;
	}
	
	
	public Book undeleteBook(UUID idBook) {
		Optional<Book> bookO = bookRepository.findById(idBook);
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			idBook.toString(),
			"Book not found."
		);
		var book = bookO.get();
		book.setDeleted(false);
		bookRepository.save(book);
		return book;
	}
	
	
	public List<Book> getAllBooks() {
		return getBooksByStatus("all");
	}
	
	
	public List<Book> getBooksByStatus(String status) {
		List<Book> booksList = null;
		boolean invalidStatus = false;
		switch (status) {
			case "all": booksList = bookRepository.findAll(); break;
			case "deleted": booksList = bookRepository.findByDeleted(); break;
			case "!deleted": booksList = bookRepository.findByNotDeleted(); break;
			case "discarded": booksList = bookRepository.findByDiscarded(); break;
			case "!discarded": booksList = bookRepository.findByNotDiscarded(); break;
			case "donated": booksList = bookRepository.findByDonated(); break;
			case "!donated": booksList = bookRepository.findByNotDonated(); break;
			case "collection": booksList = bookRepository.findByCollection(); break;
			default: invalidStatus = true;
		}
		if (invalidStatus) throw new ResourceNotFoundException(
			"B001",
			"Invalid status"
		);
		return booksList;
	}
	
	
	public List<Book> getBooksInCollection() {
		return bookRepository.findByCollection();
	}
	
	
	public Book getBook(UUID idBook) {
		Optional<Book> bookO = bookRepository.findById(idBook);
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			idBook.toString(),
			"Book not found."
		);
		return bookO.get();
	}

	
}