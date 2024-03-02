package br.com.leandro.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.BookCover;

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, UUID> {
}