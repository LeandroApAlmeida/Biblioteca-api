package br.com.leandro.library.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.LoanItem;
import br.com.leandro.library.model.LoanItemId;

@Repository
public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {
	
	@Query("SELECT li FROM LoanItem li WHERE li.loan.id = :id")
	public List<LoanItem> findAll(UUID id);
	
}