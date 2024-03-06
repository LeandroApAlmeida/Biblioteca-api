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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.leandro.library.dto.BookDto;
import br.com.leandro.library.model.Book;
import br.com.leandro.library.model.BookCover;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.BookService;
import br.com.leandro.library.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller para cadastro e manutenção de livros do acervo pessoal.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/books")
public class BookController {
	
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private LogService logService;
	
	
	/**
	 * Cadastrar um novo livro no acervo. Um livro pode ser tanto no formato físico
	 * quanto digital.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param bookDto Dados do livro.
	 * @param cover Arquivo com a capa do livro.
	 * @return Resposta padrão.
	 */
	@PostMapping
	public ResponseEntity<Response> saveBook(
		@RequestPart(name = "book") @Valid BookDto bookDto,
		@RequestPart(name = "cover", required = false) MultipartFile cover,
		HttpServletRequest request
	) {
		Book book = bookService.saveBook(bookDto, cover);
		logService.saveLog(request, "Book added: " + book.getTitle());
		Response resp = new Response();
		resp.setId(book.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Book registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Atualizar os dados de um livro.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária do livro.
	 * @param bookJson Texto em formato JSON.
	 * @param cover Arquivo com a capa do livro.
	 * @return Resposta padrão.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateBook(
		@PathVariable("id") UUID id,
		@RequestPart(name = "book") @Valid BookDto bookDto,
		@RequestPart(name = "cover", required = false) MultipartFile cover,
		HttpServletRequest request
	) {
		Book book = bookService.updateBook(id, bookDto, cover);
		logService.saveLog(request, "Book updated: " + book.getTitle());
		Response resp = new Response();
		resp.setId(book.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Book updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	/**
	 * Excluir o cadastro de um livro.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária do livro.
	 * @param delete Status de exclusão do livro. Se true, exclui o livro, se false
	 * retorna o livro (não há remoção física do registro no banco de dados).
	 * @return Resposta padrão.
	 */
	@DeleteMapping(value = "/{id}/{delete}")
	public ResponseEntity<Response> deleteBook(
		@PathVariable("id") UUID id,
		@PathVariable boolean delete,
		HttpServletRequest request
	) {
		Response resp = new Response();
		resp.setId(id.toString());
		Book book = null;
		if (delete) {
			book = bookService.deleteBook(id);
		    resp.setMessage("Book deleted successfully.");
		    logService.saveLog(request, "Book deleted: " + book.getTitle());
		} else {
			book = bookService.undeleteBook(id);
			resp.setMessage("Book undeleted successfully.");
			logService.saveLog(request, "Book undeleted: " + book.getTitle());
		}
		resp.setTime(LocalDateTime.now());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	/**
	 * Obtém todos os livros cadastrados no banco de dados.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
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
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param status Status da busca.
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
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
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
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
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