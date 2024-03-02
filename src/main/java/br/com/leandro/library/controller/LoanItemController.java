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

import br.com.leandro.library.dto.LoanItemDto;
import br.com.leandro.library.model.LoanItem;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.LoanItemService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/loanitems")
public class LoanItemController {
	
	
	@Autowired
	private LoanItemService loanItemService;
	
	
	@PostMapping
	public ResponseEntity<Response> saveLoanItem(
		@RequestBody @Valid LoanItemDto loanItemDto
	) {
		LoanItem loanItem = loanItemService.saveLoanItem(loanItemDto);
		Response resp = new Response();
		resp.setId(loanItem.getLoan().getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Cadastrado com sucesso");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateLoanItem(
		@PathVariable("id") UUID id, 
		@RequestBody @Valid LoanItemDto loanItemDto
	) {
		loanItemService.updateLoanItem(id, loanItemDto);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Alterado com sucesso");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	@DeleteMapping(value = "/{idLoan}/{idBook}")
	public ResponseEntity<Response> deleteLoanItem(
		@PathVariable("idLoan") UUID idLoan,
		@PathVariable("idBook") UUID idBook
	) {
		loanItemService.deleteLoanItem(idLoan, idBook);
		Response resp = new Response();
		resp.setId(idLoan.toString());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setMessage("Excluído com sucesso");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	@GetMapping(value = "/{idLoan}")
	public ResponseEntity<List<LoanItem>> getAllLoanItems(
		@PathVariable("idLoan") UUID idLoan
	){
		return ResponseEntity.status(HttpStatus.OK).body(
			loanItemService.getAllLoanItems(idLoan)
		);
	}
	
	
	@GetMapping(value = "/{idLoan}/{idBook}")
	public ResponseEntity<LoanItem> getLoanItem(
		@PathVariable("idLoan") UUID idLoan,
		@PathVariable("idBook") UUID idBook
	){
		return ResponseEntity.status(HttpStatus.OK).body(
			loanItemService.getLoanItem(idLoan, idBook)
		);
	}
	
	
}
