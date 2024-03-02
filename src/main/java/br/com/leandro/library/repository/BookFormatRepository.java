package br.com.leandro.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.BookFormat;

@Repository
public interface BookFormatRepository extends JpaRepository<BookFormat, Integer> {
}