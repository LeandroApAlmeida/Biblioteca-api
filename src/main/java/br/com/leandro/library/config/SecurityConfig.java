package br.com.leandro.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configura&ccedil;&otilde;es do Spring Security.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
	
	@Autowired
    private SecurityFilter securityFilter;


	/**
	 * Valida&ccedil;&atilde;o das permiss&otilde;es. No caso, o acesso ao sistema tem as seguintes restrições:<br>
	 * 
	 * <ul>
	 * 
	 * <li>
	 * Somente o usuário administrador tem acesso ao cadastro de usuários. Como é exigido que
	 * se tenha um usuário administrador, ele é inserido quando o Flyway executa o script de 
	 * atualização do banco de dados (resources/db/migration/V1_version_1.sql).
	 * </li>
	 * 
	 * <br>
	 * 
	 * <li>
	 * Somente o usuário administrador tem acessos aos registros de log do servidor.
	 * </li>
	 * 
	 * <br>
	 * 
	 * <li>
	 * Qualquer usuário tem acesso ao endpoint <i>/authenticate</i> para ter acesso ao sistema.
	 * </li>
	 * 
	 * <br>
	 * 
	 * <li>
	 * Os demais endpoints exigem que o usuário tenha sido autenticado para que possam ser
	 * acessados. Isto inclui o cadastro de livros, de empréstimos, de pessoas, etc.
	 * </li>
	 * 
	 * </ul>
	 * @param httpSecurity Dados da conexão do usuário.
	 * @return FilterChain
	 */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        // Desabilita a proteção CSRF, pois o servidor não mantém a sessão
        // do usuário.
        .csrf(csrf -> csrf.disable())
        // Não mantém a sessão do usuário, como requerido pelo padrão Rest.
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Define as regras de acesso à API de acordo com a credencial do usuário.
        .authorizeHttpRequests(authorize ->
        	authorize
	        .requestMatchers(HttpMethod.POST, "/authentication/login").permitAll()
	        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.DELETE, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PUT, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PATCH, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.POST, "/logs").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.GET, "/logs").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.DELETE, "/logs").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PUT, "/logs").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PATCH, "/logs").hasRole("ADMIN")
	        .anyRequest().authenticated()
        )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    
    /**
     * Gerenciador de autentição. Disponibiliza para injeção de dependências.
     * @param authenticationConfiguration
     * @return Gerenciador de autenticação.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * Gerador de Hash para a senha do usuário. Disponiliza para injeção de
     * dependências.
     * @return Gerador de Hash.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    
}
