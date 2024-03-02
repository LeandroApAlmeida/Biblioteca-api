package br.com.leandro.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Loan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private UUID id;
	
	@ManyToOne(targetEntity = Person.class, optional = false)
	@JoinColumn(name = "id_person")
	private Person person;
	
	@OneToMany(mappedBy = "loan")
    private List<LoanItem> items;
	
	@Column(name = "date", nullable = false)
	private LocalDateTime date;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "last_update_date", nullable = false)
	private LocalDateTime lastUpdateDate;
	
	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	public Loan(UUID id, Person person, LocalDateTime date, String notes, LocalDateTime lastUpdateDate) {
		super();
		this.id = id;
		this.person = person;
		this.date = date;
		this.notes = notes;
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
}
