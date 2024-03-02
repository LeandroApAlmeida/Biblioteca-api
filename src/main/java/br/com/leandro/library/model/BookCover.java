package br.com.leandro.library.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_cover")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookCover implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "file_path")
	private String filePath;
	
	@Transient
	private byte[] data;
	
	
	public BookCover(UUID id, String filePath) {
		this.id = id;
		this.filePath = filePath;
	}
	

}
