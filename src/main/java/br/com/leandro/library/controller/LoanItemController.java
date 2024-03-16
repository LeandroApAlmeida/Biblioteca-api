package br.com.leandro.library.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import br.com.leandro.library.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller para a manutenção de itens de empréstimo.
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/loanitems")
public class LoanItemController {
	
	
	@Autowired
	private LoanItemService loanItemService;
	
	@Autowired
	private LogService logService;
	
	
	/**
	 * Salvar item de empréstimo.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param loanItemDtoList Lista com o itens de empréstimo a serem cadastrados.
	 * @return Lista de resposta padrão, uma para cada item passado para o método.
	 */
	@PostMapping
	public ResponseEntity<List<Response>> saveLoanItem(
		@RequestBody @Valid List<LoanItemDto> loanItemDtoList,
		HttpServletRequest request
	) {
		List<LoanItem> loanItemList = loanItemService.saveLoanItem(loanItemDtoList);
		List<Response> responseList = new ArrayList<>(loanItemList.size());
		for (LoanItem loanItem : loanItemList) {
			logService.saveLog(request, "Loan item added: " + loanItem.getBook().getTitle());
		}
		Response resp = new Response();
		resp.setId("");
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Loan item successfully registered.");
		resp.setTime(LocalDateTime.now());
		responseList.add(resp);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
	}
	
	
	/**
	 * Atualizar os dados do item de empréstimo.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária.
	 * @param loanItemDto Dados do item de empréstimo.
	 * @return Resposta padrão.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateLoanItem(
		@PathVariable("id") UUID id, 
		@RequestBody @Valid LoanItemDto loanItemDto,
		HttpServletRequest request
	) {
		LoanItem loanItem = loanItemService.updateLoanItem(id, loanItemDto);
		logService.saveLog(request, "Loan item updated: " + loanItem.getBook().getTitle());
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Loan item successfully updated.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir o item de empréstimo.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param idLoan Identificador chave primária do empréstimo.
	 * @param idBook Identificador chave primária do livro.
	 * @return Resposta padrão.
	 */
	@DeleteMapping(value = "/{idLoan}/{idBook}")
	public ResponseEntity<Response> deleteLoanItem(
		@PathVariable("idLoan") UUID idLoan,
		@PathVariable("idBook") UUID idBook,
		HttpServletRequest request
	) {
		loanItemService.deleteLoanItem(idLoan, idBook);
		logService.saveLog(request, "Loan item deleted: " + idLoan.toString());
		Response resp = new Response();
		resp.setId(idLoan.toString());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setMessage("Loan item successfully deleted.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obter todos os itens de um determinado empréstimo.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param idLoan Identificador chave primária do empréstimo.
	 * @return Lista com os itens de empréstimo.
	 */
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
