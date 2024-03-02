package br.com.leandro.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.leandro.library.model.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, String> {
}