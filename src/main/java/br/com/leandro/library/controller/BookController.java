package br.com.leandro.library.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.leandro.library.dto.BookDto;
import br.com.leandro.library.dto.JSonFormat;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.BookCover;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.BookService;
import jakarta.validation.Valid;

/**
 * Controller para cadastro e manutenção de livros do acervo pessoal.
 * <br><br>
 * Nível de Acesso: USER
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/books")
public class BookController {
	
	
	@Autowired
	private BookService bookService;
	
	
	/**
	 * Parser do texto para o formato JSON.
	 * @param bookJson
	 * @return
	 */
	private BookDto jsonToBookDto(String bookJson) {
		JSONObject json = new JSONObject(bookJson);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
			JSonFormat.DATE_FORMAT
		);
		return new BookDto(
			UUID.fromString(json.getString("id")),
			json.getInt("idFormat"),
			json.getString("title"),
			json.getString("subtitle"),
			json.getString("author"),
			json.getString("publisher"),
			json.getString("isbn"),
			json.getInt("edition"),
			json.getInt("volume"),
			json.getInt("releaseYear"),
			json.getInt("numberOfPages"),
			json.getString("summary"),
			LocalDateTime.parse(json.getString("acquisitionDate"), formatter),
			LocalDateTime.parse(json.getString("registrationDate"), formatter),
			LocalDateTime.parse(json.getString("lastUpdateDate"), formatter)
		);
	}
	
	
	/**
	 * Cadastrar um novo livro no acervo. Um livro pode ser tanto no formato físico
	 * quanto digital.
	 * @param bookDto Texto em formato JSON.
	 * @param cover Arquivo com a capa do livro.
	 * @return Resposta padrão.
	 */
	@PostMapping
	public ResponseEntity<Response> saveBook(
		@RequestBody @Valid @RequestParam("book") String bookJson,
		@RequestParam(required = false) MultipartFile cover
	) {
		BookDto bookDto = jsonToBookDto(bookJson);
		Book book = bookService.saveBook(bookDto, cover);
		Response resp = new Response();
		resp.setId(book.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Book registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Atualizar os dados de cadastro de um livro.
	 * @param id Identificador chave primária do livro.
	 * @param bookJson Texto em formato JSON.
	 * @param cover Arquivo com a capa do livro.
	 * @return Resposta padrão.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateBook(
		@PathVariable("id") UUID id,
		@RequestBody @Valid @RequestParam("book") String bookJson,
		@RequestParam(required = false) MultipartFile cover
	) {
		BookDto bookDto = jsonToBookDto(bookJson);
		Book book = bookService.updateBook(id, bookDto, cover);
		Response resp = new Response();
		resp.setId(book.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Book updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir o cadastro de um livro.
	 * @param id Identificador chave primária do livro.
	 * @param delete Status de exclusão do livro. Se true, exclui. Se false retorna
	 * o livro (não remoção física do registro no banco de dados).
	 * @return Resposta padrão
	 */
	@DeleteMapping(value = "/{id}/{delete}")
	public ResponseEntity<Response> deleteBook(
		@PathVariable("id") UUID id,
		@PathVariable boolean delete
	) {
		Response resp = new Response();
		resp.setId(id.toString());
		if (delete) {
			bookService.deleteBook(id);
		    resp.setMessage("Book deleted successfully.");
		} else {
			bookService.undeleteBook(id);
			resp.setMessage("Book undeleted successfully.");
		}
		resp.setTime(LocalDateTime.now());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obtém todos os livros cadastrados no banco de dados.
	 * @return Lista com todos os livros cadastrados no banco de dados.
	 */
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks(){
		return ResponseEntity.status(HttpStatus.OK).body(
			bookService.getAllBooks()
		);
	}
	
	
	/**
	 * Obtém todos os livros cadastrados no banco de dados baseado em um status
	 * de busca.
	 * @return Lista com todos os livros cadastrados no banco de dados baseado
	 * no status.
	 */
	@GetMapping(value = "/status/{status}")
	public ResponseEntity<List<Book>> getBooksByStatus(
		@PathVariable("status") String status
	){
		return ResponseEntity.status(HttpStatus.OK).body(
			bookService.getBooksByStatus(status)
		);
	}
	
	
	/**
	 * Obtém um livro específico de acordo com seu identificador chave primária.
	 * @param id Identificador chave primária do livro.
	 * @return Dados do livro pesquisado.
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Book> getBook(
		@PathVariable("id") UUID id
	) {
		return ResponseEntity.status(HttpStatus.OK).body(
			bookService.getBook(id)
		);
	}
	
	
	/**
	 * Obter a capa da livro de acordo com seu identificador chave primária.
	 * @param id Identificador chave primária do livro.
	 * @return Capa do livro.
	 */
	@GetMapping(value = "/cover/{id}")
	public ResponseEntity<BookCover> getCover(
		@PathVariable("id") UUID id
	) {
		Book book = bookService.getBook(id);
		BookCover cover = book.getCover();
		byte[] data = bookService.getCover(cover.getId());
		cover.setData(data);
		return ResponseEntity.status(HttpStatus.OK).body(cover);
	}
	
	
}