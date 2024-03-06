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

import br.com.leandro.library.dto.DiscardedBookDto;
import br.com.leandro.library.model.DiscardedBook;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.DiscardedBookService;
import br.com.leandro.library.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller para manutenção de livros descartados. 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/discardedbooks")
public class DiscardedBookController {
	
	
	@Autowired
	private DiscardedBookService discardedBookService;
	
	@Autowired
	private LogService logService;
	
	
	/**
	 * Salvar dados de um livro descartado.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param discardedBookDto Dados do livro descartado.
	 * @return Resposta padrão.
	 */
	@PostMapping
	public ResponseEntity<Response> saveDiscardedBook(
		@RequestBody @Valid DiscardedBookDto discardedBookDto,
		HttpServletRequest request
	) {
		DiscardedBook discardedBook = discardedBookService.saveDiscardedBook(
			discardedBookDto
		);
		logService.saveLog(
			request,
			"Discarded book added: " +
			discardedBook.getBook().getTitle()
		);
		Response resp = new Response();
		resp.setId(discardedBook.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Discarded book registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Atualizar dados de um livro descartado.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária do livro descartado.
	 * @param discardedBookDto Dados para atualização do livro descartado.
	 * @return Resposta padrão.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateDiscardedBook(
		@PathVariable("id") UUID id,
		@RequestBody @Valid DiscardedBookDto discardedBookDto,
		HttpServletRequest request
	) {
		DiscardedBook discardedBook = discardedBookService.updateDiscardedBook(
			id,
			discardedBookDto
		);
		logService.saveLog(
			request,
			"Discarded book updated: " +
			discardedBook.getBook().getTitle()
		);
		Response resp = new Response();
		resp.setId(discardedBookDto.idBook().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Discarded book updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir o livro descartado, fazendo com que passe ao estado de livro no
	 * acervo novamente.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária do livro descartado.
	 * @return Resposta padrão.
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> deleteDiscardedBook(
		@PathVariable("id") UUID id,
		HttpServletRequest request
	) {
		DiscardedBook discardedBook = discardedBookService.getDiscardedBook(id);
		discardedBookService.deleteDiscardedBook(id);
		logService.saveLog(
			request,
			"Discarded book deleted: " +
			discardedBook.getBook().getTitle()
		);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setMessage("Discarded book deleted successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obter todos os livros descartados.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @return Lista com todos os livros descartados.
	 */
	@GetMapping
	public ResponseEntity<List<DiscardedBook>> getAllDiscardedBooks(){
		return ResponseEntity.status(HttpStatus.OK).body(
			discardedBookService.getAllDiscardedBooks()
		);
	}
	
	
	/**
	 * Obter um livro descartado de acordo com seu identificador
	 * chave primária.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária.
	 * @return Dados do livro descartado.
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<DiscardedBook> getDiscardedBook(
		@PathVariable("id") UUID id
	){
		return ResponseEntity.status(HttpStatus.OK).body(
			discardedBookService.getDiscardedBook(id)
		);
	}
	
	
}