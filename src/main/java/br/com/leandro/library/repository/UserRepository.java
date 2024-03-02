package br.com.leandro.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	UserDetails findByUserName(String userName);
}