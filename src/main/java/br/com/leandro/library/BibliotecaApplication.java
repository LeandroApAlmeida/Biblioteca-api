package br.com.leandro.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Inicializa&ccedil;&atilde;o do servidor.
 */
@SpringBootApplication
public class BibliotecaApplication {

	
	/**
	 * Sabe o servidor com todos as bibliotecas importadas e o Apache Tomcat.
	 * @param args N&atilde;o trata argumentos de linha de comando.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	
}
