package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.LoanItemDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.Loan;
import br.com.leandro.library.model.LoanItem;
import br.com.leandro.library.model.LoanItemId;
import br.com.leandro.library.repository.BookRepository;
import br.com.leandro.library.repository.LoanItemRepository;
import br.com.leandro.library.repository.LoanRepository;

@Service
public class LoanItemService {
	
	
	@Autowired
	private LoanItemRepository loanItemRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	public LoanItem saveLoanItem(LoanItemDto loanItemDto) {
		Optional<Loan> loanO = loanRepository.findById(loanItemDto.idloan());
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			loanItemDto.idloan().toString(),
			"Loan not found."
		);
		Optional<Book> bookO = bookRepository.findById(loanItemDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			loanItemDto.idloan().toString(),
			"Book not found."
		);
		LoanItem loanItem = new LoanItem(
			loanO.get(),
			bookO.get(),
			false,
			null,
			loanItemDto.notes(),
			loanItemDto.lastUpdateDate(),
			false
		);
		return loanItemRepository.save(loanItem);
	}
	
	
	public LoanItem updateLoanItem(UUID id, LoanItemDto loanItemDto) {
		Optional<Loan> loanO = loanRepository.findById(loanItemDto.idloan());
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Loan not found."
		);
		Optional<Book> bookO = bookRepository.findById(loanItemDto.idBook());
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Book not found."
		);
		LoanItemId loanItemId = new LoanItemId(loanO.get(), bookO.get());
		Optional<LoanItem> loanItemO = loanItemRepository.findById(loanItemId);
		if (loanItemO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Loan item not found."
		);
		LoanItem loanItem = loanItemO.get();
		loanItem.setBook(bookO.get());
		loanItem.setNotes(loanItemDto.notes());
		loanItem.setReturnDate(loanItemDto.returnDate());
		loanItem.setReturned(loanItemDto.returned());
		loanItem.setLastUpdateDate(loanItemDto.lastUpdateDate());
		return loanItemRepository.save(loanItem);
	}
	
	
	public void deleteLoanItem(UUID idLoan, UUID idBook) {
		Optional<Loan> loanO = loanRepository.findById(idLoan);
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			idLoan.toString(),
			"Loan not found."
		);
		Optional<Book> bookO = bookRepository.findById(idBook);
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			idBook.toString(),
			"Book not found."
		);
		LoanItemId loanItemId = new LoanItemId(loanO.get(), bookO.get());
		Optional<LoanItem> loanItemO = loanItemRepository.findById(loanItemId);
		if (loanItemO.isEmpty()) throw new ResourceNotFoundException(
			idLoan.toString(),
			"Loan item not found."
		);
		loanItemRepository.delete(loanItemO.get());
	}
	
	
	public List<LoanItem> getAllLoanItems(UUID idLoan) {
		return loanItemRepository.findAll(idLoan);
	}
	
	
	public LoanItem getLoanItem(UUID idLoan, UUID idBook) {
		Optional<Loan> loanO = loanRepository.findById(idLoan);
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			idLoan.toString(),
			"Loan not found."
		);
		Optional<Book> bookO = bookRepository.findById(idBook);
		if (bookO.isEmpty()) throw new ResourceNotFoundException(
			idBook.toString(),
			"Book not found."
		);
		LoanItemId loanItemId = new LoanItemId(loanO.get(), bookO.get());
		Optional<LoanItem> loanItemO = loanItemRepository.findById(loanItemId);
		if (loanItemO.isEmpty()) throw new ResourceNotFoundException(
			idLoan.toString(),
			"Loan item not found."
		);
		return loanItemO.get();
	}
	
	
}