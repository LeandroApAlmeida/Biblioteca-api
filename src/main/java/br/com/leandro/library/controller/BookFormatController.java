package br.com.leandro.library.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.library.model.BookFormat;
import br.com.leandro.library.service.BookFormatService;

/**
 * Controller para recuperação de dados de formato de livro.
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping(value = "/bookformats")
public class BookFormatController {
	
	
	@Autowired
	private BookFormatService bookFormatService;
	
	
	/**
	 * Obter todos os formatos de livro cadastrados no banco de dados.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @return Lista com todos os formatos de livro cadastrado no banco de dados.
	 */
	@GetMapping
	public ResponseEntity<List<BookFormat>> getAllBookFormats(){
		return ResponseEntity.status(HttpStatus.OK).body(
			bookFormatService.getAllBookFormats()
		);
	}
	
	
	/**
	 * Obter um formato de livro específico de acordo com o identificador
	 * chave primária.
	 * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
	 * @param id Identificador chave primária.
	 * @return Dados do formato de livro pesquisado.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<BookFormat> getBookFormat(
		@PathVariable int id
	){
		return ResponseEntity.status(HttpStatus.OK).body(
			bookFormatService.getBookFormat(id)
		);
	}
	
	
}