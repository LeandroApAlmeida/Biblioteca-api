package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.LoanDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Loan;
import br.com.leandro.library.model.Person;
import br.com.leandro.library.repository.LoanRepository;
import br.com.leandro.library.repository.PersonRepository;

@Service
public class LoanService {
	
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	
	public Loan saveLoan(LoanDto loanDto) {
		Optional<Person> personO = personRepository.findById(loanDto.idPerson());
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			loanDto.id().toString(),
			"Person not found."
		);
		Loan loan = new Loan(
			loanDto.id(),
			personO.get(),
			loanDto.date(),
			loanDto.notes(),
			loanDto.lastUpdateDate()
		);
		return loanRepository.save(loan);
	}
	
	
	public Loan updateLoan(UUID id, LoanDto loanDto) {
		Optional<Loan> loanO = loanRepository.findById(id);
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			loanDto.id().toString(),
			"Loan not found."
		);
		Optional<Person> personO = personRepository.findById(loanDto.idPerson());
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			loanDto.id().toString(),
			"Person not found."
		);
		Loan loan = loanO.get();
		loan.setPerson(personO.get());
		loan.setNotes(loanDto.notes());
		loan.setDate(loanDto.date());
		loan.setLastUpdateDate(loanDto.lastUpdateDate());
		return loanRepository.save(loan);
	}
	
	
	public void deleteLoan(UUID id) {
		Optional<Loan> loanO = loanRepository.findById(id);
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Loan not found."
		);
		Loan loan = loanO.get();
		loan.setDeleted(true);
		loanRepository.save(loan);
	}
	
	
	public List<Loan> getAllLoans() {
		return loanRepository.findAll();
	}
	
	
	public Loan getLoan(UUID id) {
		Optional<Loan> loanO = loanRepository.findById(id);
		if (loanO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Loan not found."
		);
		return loanO.get();
	}
	

}
