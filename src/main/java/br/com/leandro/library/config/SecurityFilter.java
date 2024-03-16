package br.com.leandro.library.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.leandro.library.repository.UserRepository;
import br.com.leandro.library.service.TokenService;

import java.io.IOException;


/**
 * Filtro para a validação das credencias de acesso do usuário.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Override
    protected void doFilterInternal(
    	HttpServletRequest request,
    	HttpServletResponse response, 
    	FilterChain filterChain
	) throws ServletException, IOException {
    	// Obtém o token passado na conexão. Uso o token de acesso (Bearer) padrão,
    	// que tem duração determinada.
        String token = recoverToken(request);
        if(token != null){
        	// O Token deve ser validado. O nome do usuário foi passado como
        	// subject. A validação retorna este nome de usuário. Desta forma
        	// será recuperado o objeto do usuário para acesso aos dados.
            String userName = tokenService.validateToken(token);
            UserDetails user = userRepository.findByUserName(userName);
            if (user != null) {
            	// Verifica se o usuário não foi bloqueado. Feito isso, segue
            	// à validação de suas permissões de acesso para o endpoint
            	// solicitado.
            	if (user.isEnabled()) {
		            UsernamePasswordAuthenticationToken authentication = 
            		new UsernamePasswordAuthenticationToken(
		            	user,
		            	null,
		            	user.getAuthorities()
		        	);
		            SecurityContextHolder.getContext().setAuthentication(
		        		authentication
		    		);
            	}
            }
        }
        filterChain.doFilter(request, response);
    }
    
    
    /**
     * Recupera o token do cabeçalho do HTTP.
     * @param request Dados da conexão do usuário.
     * @return Token extraído do cabeçalho HTTP.
     */
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
    
}
