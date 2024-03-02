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

import br.com.leandro.library.dto.DonatedBookDto;
import br.com.leandro.library.model.DonatedBook;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.DonatedBookService;
import jakarta.validation.Valid;

/**
 * Controller para manutenção de livros doados.
 */
@RestController
@RequestMapping(value = "/donatedbooks")
public class DonatedBookController {
	
	
	@Autowired
	private DonatedBookService donatedBookService;
	
	
	/**
	 * Salvar dados de um livro doado.
	 * @param donatedBookDto Dados do livro doado.
	 * @return Dados do livro doado.
	 */
	@PostMapping
	public ResponseEntity<Response> saveDonatedBook(
		@RequestBody @Valid DonatedBookDto donatedBookDto
	) {
		DonatedBook donatedBook = donatedBookService.saveDonatedBook(
			donatedBookDto
		);
		Response resp = new Response();
		resp.setId(donatedBook.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Donated book registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Atualizar dados do livro doado.
	 * @param id Identificador chave primária do livro doado.
	 * @param donatedBookDto Dados atualizados do livro doado.
	 * @return Dados atualizados do livro doado.
	 */
	@PutMapping(value = "/update/{id}")
	public ResponseEntity<Response> updateDonatedBook(
		@PathVariable("id") UUID id, 
		@RequestBody @Valid DonatedBookDto donatedBookDto
	) {
		DonatedBook donatedBook = donatedBookService.updateDonatedBook(
			id,
			donatedBookDto
		);
		Response resp = new Response();
		resp.setId(donatedBook.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Donated book updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir registro de um livro doado.
	 * @param id Identificador chave primária do livro doado.
	 * @param donatedBookDto
	 * @return
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Response> deleteDonatedBook(
		@PathVariable("id") UUID id
	) {
		donatedBookService.deleteDonatedBook(id);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setMessage("Donated book registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obter todos os livros que foram doados.
	 * @return Lista de todos os livros que foram doados.
	 */
	@GetMapping
	public ResponseEntity<List<DonatedBook>> getAllDonatedBooks(){
		return ResponseEntity.status(HttpStatus.OK).body(
			donatedBookService.getAllDonatedBooks()
		);
	}
	
	
	/**
	 * Obter livro doado de acordo com seu identificador chave primária.
	 * @param id Identificador chave primária.
	 * @return Dados do livro doado.
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<DonatedBook> getDonatedBook(
		@PathVariable("id") UUID id
	) {
		return ResponseEntity.status(HttpStatus.OK).body(
			donatedBookService.getDonatedBook(id)
		);
	}
	
	
}