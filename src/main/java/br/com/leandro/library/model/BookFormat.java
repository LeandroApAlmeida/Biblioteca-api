package br.com.leandro.library.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Formato do livro.
 */
@Entity
@Table(name = "book_format")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookFormat implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**Identificador chave primária.*/
	@Id
	@Column(name = "id")
	private int id;
	
	/**Descrição do formato.*/
	@Column(name = "description", nullable = false)
	@NotBlank(message = "Descrição deve ser preenchida")
	private String description;

}
