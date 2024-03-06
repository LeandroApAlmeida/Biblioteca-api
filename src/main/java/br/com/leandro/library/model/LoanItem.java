package br.com.leandro.library.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(LoanItemId.class)
@Table(name = "loan_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"loan", "book"})
public class LoanItem {
	
	@Id
	@ManyToOne(targetEntity = Loan.class, optional = false)
	@JoinColumn(name = "id_loan")
	private Loan loan;
	
	@Id
	@ManyToOne(targetEntity = Book.class, optional = false)
	@JoinColumn(name = "id_book")
	private Book book;
	
	@Column(name = "returned", nullable = false)
	private boolean returned;
	
	@Column(name = "return_date")
	private LocalDateTime returnDate;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "last_update_date", nullable = false)
	private LocalDateTime lastUpdateDate;
	
	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

}