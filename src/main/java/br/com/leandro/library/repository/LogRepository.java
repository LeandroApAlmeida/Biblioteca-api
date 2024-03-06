package br.com.leandro.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
}