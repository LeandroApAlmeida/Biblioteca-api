package br.com.leandro.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

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

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false)
	private UUID id;
	
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Nome da pessoa deve ser preenchido")
	private String name;
	
	@Column(name = "description", nullable = false)
	@NotBlank(message = "Descrição da pessoa deve ser preenchido")
	private String description;
	
	@Column(name = "registration_date", nullable = false)
	private LocalDateTime registrationDate;
	
	@Column(name = "last_update_date", nullable = false)
	private LocalDateTime lastUpdateDate;
	
	@Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

}
