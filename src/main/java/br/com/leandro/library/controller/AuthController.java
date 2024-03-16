package br.com.leandro.library.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.library.dto.UserDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.User;
import br.com.leandro.library.response.LoginResponse;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.LogService;
import br.com.leandro.library.service.TokenService;
import br.com.leandro.library.service.UserService;
import br.com.leandro.library.session.LoggedOutTokenCache;
import br.com.leandro.library.session.SessionCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Controller para autenticação do acesso ao sistema.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping("/authentication")
public class AuthController {
	
	
	@Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private LogService logService;
    
    @Autowired
    private SessionCookie sessionCookie;
    
    @Autowired
    private LoggedOutTokenCache loggedOutTokenCache;
    
    
    /**
     * Autenticar o usuário, retornando o token para acesso aos recursos do servidor.
     * @param userDto Dados do usuário.
     * @return Token.
     */
    private String authenticate(UserDto userDto) {
    	String token = null;
    	UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
			userDto.userName(),
			userDto.password()
		);
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
    	if (authentication.isAuthenticated()) {
    		token = tokenService.generateToken((User) authentication.getPrincipal());
    	}
    	return token;
    }
    
    
    /**
     * Fazer o login no sistema. Como a arquitetura é baseada em API Rest, não
     * será mantida informação de estado de sessão, e, assim que validado o 
     * usuário, este receberá um token que controlará o acesso aos recursos
     * do sistema.
     * <br><br>
	 * Nível de Acesso: <b><i>ADMIN / USER</i></b>
     * @param userDto Informações de login do usuário.
     * @param request Informações sobre a requisição HTTP.
     * @param response Informações sobre a requisição HTTP.
     * @return Resposta padrão contendo o token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
    	@RequestBody @Valid UserDto userDto,
    	HttpServletRequest request,
    	HttpServletResponse response
    ){
    	String token = authenticate(userDto);
    	if (token != null) {
    		User user = (User) userService.loadUserByUsername(userDto.userName());
    		response.addCookie(sessionCookie.createCookie(token));
    		logService.saveLog(user, request, "Login: " + user.getUsername());
    		LoginResponse resp = new LoginResponse();
    		resp.setId(user.getId().toString());
    		resp.setToken(token);
    		resp.setTime(LocalDateTime.now());
    		return ResponseEntity.status(HttpStatus.OK).body(resp);
    	} else {
    		throw new ResourceNotFoundException(
				userDto.userName(),
				"Invalid username or password."
			);
    	}
    }
    
    
    /**
     * Fazer o logout no sistema.
     * @param request Informações sobre a requisição HTTP.
     * @param response Informações sobre a requisição HTTP.
     * @return Resposta padrão.
     */
    @GetMapping("/logout")
    public ResponseEntity<Response> logOut(
    	HttpServletRequest request,
    	HttpServletResponse response
	){
        response.addCookie(sessionCookie.deleteCookie());
        User user = null;
        try {
			String token = tokenService.recoverToken(request);
	        if(token != null){
	            String userName = tokenService.validateToken(token);
	            UserDetails userDetails = userService.getUser(userName);
	            if (userDetails != null) {
	            	user = (User) userDetails;
	            }
	        }
	        logService.saveLog(request, "Logout");
	        loggedOutTokenCache.addToken(token);
			Response resp = new Response();
			resp.setId(user.getId().toString());
			resp.setMessage("Logout");
			resp.setStatus(String.valueOf(HttpStatus.OK.value()));
			resp.setTime(LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.OK).body(resp);
		} catch (Exception ex) {
			throw new ResourceNotFoundException(
				"",
				"Failed logout."
			);
		}
    }
	
	
}