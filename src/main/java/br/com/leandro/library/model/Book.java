package br.com.leandro.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidade que representa um livro no banco de dados.
 */
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	/**Identificador chave primária.*/
	@Id
	@Column(name = "id")
    private UUID id;
	
	/**Formato do livro. Pode ser formato físico, PDF, epub.*/
	@ManyToOne(targetEntity = BookFormat.class, optional = false)
	@JoinColumn(name = "id_format")
    private BookFormat format;
	
	@OneToOne(targetEntity = BookCover.class, optional = false)
	@JoinColumn(name = "id_cover")
	private BookCover cover;

	/**Título do livro.*/
    @Column(name = "title", nullable = false)
    @NotBlank
    private String title;

    /**Subtítulo do livro.*/
    @Column(name = "subtitle")
    private String subtitle;

    /**Autor do livro.*/
    @Column(name = "author", nullable = false)
    @NotBlank
    private String author;

    /**Editora do livro.*/
    @Column(name = "publisher", nullable = false)
    @NotBlank
    private String publisher;

    /**ISBN do livro.*/
    @Column(name = "isbn", nullable = false)
    @NotBlank
    private String isbn;

    /**Edição do livro.*/
    @Column(name = "edition", nullable = false)
    private int edition;

    /**Volume do livro.*/
    @Column(name = "volume", nullable = false)
    private int volume;

    /**Ano de lançamento do livro.*/
    @Column(name = "release_year", nullable = false)
    private int releaseYear;
    
    /**Número de páginas do livro.*/
    @Column(name = "number_of_pages", nullable = false) 
    private int numberOfPages;

    /**Sumário do livro.*/
    @Column(name = "summary")
    private String summary;
    
    /**Ano de aquisição do livro.*/
    @Column(name = "acquisition_date", nullable = false)
    private LocalDateTime acquisitionDate;

    /**Data de cadastro do livro.*/
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    /**Data da última modificação de cadastro do livro.*/
    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;
    
    /**Status de exclusão do livro. Se true, foi excluído. Se false não foi excluído.*/
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    
}
