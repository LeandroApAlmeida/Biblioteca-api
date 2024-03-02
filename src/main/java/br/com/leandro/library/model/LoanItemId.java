package br.com.leandro.library.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LoanItemId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Loan loan;
	
	private Book book;

}