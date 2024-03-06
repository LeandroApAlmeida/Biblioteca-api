package br.com.leandro.library.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.library.dto.LoanDto;
import br.com.leandro.library.model.Loan;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.LoanService;
import jakarta.validation.Valid;

/**
 * Controller para manutenção de empréstimos.
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/loans")
public class LoanController {
	
	
	@Autowired
	private LoanService loanService;
	
	
	/**
	 * Salvar dados do empréstimo realizado.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param loanDto Dados do empréstimo
	 * @return Resposta padrão.
	 */
	@PostMapping
	public ResponseEntity<Response> saveLoan(
		@RequestBody @Valid LoanDto loanDto
	) {
		Loan loan = loanService.saveLoan(loanDto);
		Response resp = new Response();
		resp.setId(loan.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Loan registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Atualizar dados do empréstimo realizado.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária.
	 * @param loanDto Dados do empréstimo.
	 * @return Resposta padrão.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateLoan(
		@PathVariable(name = "id") UUID id, 
		@RequestBody @Valid LoanDto loanDto
	) {
		loanService.updateLoan(id, loanDto);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Loan updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir o empréstimo. Não há remoção física do registro no banco de dados.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária.
	 * @return Resposta padrão.
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> deleteLoan(
		@PathVariable("id") UUID id
	) {
		loanService.deleteLoan(id);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setMessage("Excluído com sucesso.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obter todos os empréstimos.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @return Lista com todos os empréstimos.
	 */
	@GetMapping
	public ResponseEntity<List<Loan>> getAllLoans(){
		return ResponseEntity.status(HttpStatus.OK).body(
			loanService.getAllLoans()
		);
	}
	
	
	/**
	 * Obter um empréstimo de acordo com seu identificador chave primária.
	 * @param id Identificador chave primária.
	 * @return Dados do empréstimo.
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Loan> getLoan(
		@PathVariable("id") UUID id
	) {
		return ResponseEntity.status(HttpStatus.OK).body(
			loanService.getLoan(id)
		);
	}
	
	
}