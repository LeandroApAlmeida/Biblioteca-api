package br.com.leandro.library.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    
	
	@Autowired
    private SecurityFilter securityFilter;


	/**
	 * Validação de acesso. No caso, o acesso ao sistema tem as seguintes restrições:<br>
	 * <ul>
	 * <li>Somente o usuário administrador tem acesso ao cadastro de usuários. Como é exigido que
	 * se tenha um usuário administrador, ele é inserido quando o Flyway executa o script de 
	 * atualização do banco de dados.</li>
	 * <br>
	 * <li>Qualquer usuário tem acesso ao endpoint <i>/users/login</i> para ter acesso ao sistema.</li>
	 * <br>
	 * <li>Os demais endpoints exigem que o usuário tenha sido autenticado para que possam ser
	 * acessados. Isto inclui o cadastro de livros, de empréstimos, de pessoas, etc.</li>
	 * </ul>
	 * @param httpSecurity
	 * @return
	 * @throws Exception
	 */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
	        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
	        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.DELETE, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PUT, "/users").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.PATCH, "/users").hasRole("ADMIN")
	        .anyRequest().authenticated()
        )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    
}
