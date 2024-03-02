package br.com.leandro.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.Book;
import java.util.List;



@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
	
	
	@Query("""
	SELECT
		b
	FROM Book b 
	WHERE b.isDeleted = true
	ORDER BY b.title ASC
	""")
	List<Book> findByDeleted();
	
	
	@Query("""
	SELECT
		b
	FROM Book b 
	WHERE b.isDeleted = false
	ORDER BY b.title ASC
	""")
	List<Book> findByNotDeleted();
	
	
	@Query("""
	SELECT 
		b
	FROM Book b 
	WHERE b.isDeleted = false AND 
	b.id NOT IN (
		SELECT
			db.id
		FROM DiscardedBook db
	)
	ORDER BY b.title ASC
	""")
	List<Book> findByCollection();
	
	
	@Query("""
	SELECT 
		b
	FROM Book b 
	WHERE b.isDeleted = false AND
	b.id IN (
		SELECT
			db.id
		FROM DonatedBook db
	)
	ORDER BY b.title ASC
	""")
	List<Book> findByDonated();
	
	
	@Query("""
	SELECT 
		b
	FROM Book b 
	WHERE b.isDeleted = false AND
	b.id NOT IN (
		SELECT
			db.id
		FROM DonatedBook db
	)
	ORDER BY b.title ASC
	""")
	List<Book> findByNotDonated();
	
	
	@Query("""
	SELECT 
		b
	FROM Book b 
	WHERE b.isDeleted = false AND
	b.id IN (
		SELECT
			db.id
		FROM DiscardedBook db
	)
	ORDER BY b.title ASC
	""")
	List<Book> findByDiscarded();
	
	
	@Query("""
	SELECT 
		b
	FROM Book b 
	WHERE b.isDeleted = false AND
	b.id NOT IN (
		SELECT
			db.id
		FROM DiscardedBook db
	)
	ORDER BY b.title ASC
	""")
	List<Book> findByNotDiscarded();
	
	
}