package br.com.leandro.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

/**
 * Controller para manutenção de usuários e login no sistema. Algumas funcionalidades
 * têm a exigência de que o usuário seja administrador do sistema para que se tenha
 * acesso.
 * 
 * <br><br>
 * 
 * As funcionalidades a nível de administrador são:
 * 
 * <br>
 * <ul>
 * <li>Cadastro de novos usuários.</li>
 * <li>Desativação/reativação de usuários.</li>
 * <li>Listagem de usuários.</li> 
 * </ul>
 * 
 * As funcionalidades de acesso irrestrito são:
 * 
 * <br><br>
 * <ul>
 * <li>Fazer o login.</li>
 * </ul>
 * 
 * As funcionalidades que qualquer usuário que está em sessão tem acesso são:
 * 
 * <br><br>
 * <ul>
 * <li>Alteração das credenciais de acesso (nome de usuário e senha).
 * </ul>
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@RestController
@RequestMapping("users")
public class UserController {
	
    
	@Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private LogService logService;
    
    
    /**
     * Autenticar o usuário, retornando o token para acesso.
     * @param userName Nome de usuário.
     * @param password Senha de acesso.
     * @return Token.
     */
    private String authenticate(String userName, String password) {
    	String token = null;
    	UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
			userName,
			password
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
	 * Nível de Acesso: <b><i>USER</i></b>
     * @param userName Nome de usuário.
     * @param password Senha de acesso.
     * @param request Informações sobre a requisição HTTP.
     * @return Resposta padrão contendo o token.
     */
    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<LoginResponse> login(
    	@PathVariable("username") String userName,
    	@PathVariable("password") String password,
    	HttpServletRequest request
    ){
    	String token = authenticate(userName, password);
    	if (token != null) {
    		User user = (User) userService.loadUserByUsername(userName);
    		logService.saveLog(user, request, "Login: " + user.getUsername());
    		LoginResponse resp = new LoginResponse();
    		resp.setId(user.getId().toString());
    		resp.setToken(token);
    		resp.setTime(LocalDateTime.now());
    		return ResponseEntity.status(HttpStatus.OK).body(resp);
    	} else {
    		throw new ResourceNotFoundException(
				userName,
				"Invalid username or password."
			);
    	}
    }
    
    
    /**
     * Cadastrar um novo usuário do sistema. Todo o usuário cadastrado aqui terá
     * privilégio de USER (ver: {@link User.Role}), pois o sistema aceita somente 1
     * usuário administrador.
     * <br><br>
	 * Nível de Acesso: <b><i>ADMIN</i></b>
     * @param userDto Dados do usuário cadastrado.
     * @param request request Informações sobre a requisição HTTP.
     * @return Resposta padrão.
     */
    @PostMapping
    public ResponseEntity<Response> saveUser(
    	@RequestBody @Valid UserDto userDto,
    	HttpServletRequest request
	){
    	User user = userService.saveUser(userDto);
    	logService.saveLog(request, "User added: " + user.getUsername());
    	Response resp = new Response();
		resp.setId(user.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("User registered successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
    
    
    /**
     * Atualizar as credenciais de acesso de um usuário do sistema. Neste
     * caso, fornece a capacidade de o usuário administrador alterar os
     * dados de qualquer usuário.
     * <br><br>
	 * Nível de Acesso: <b><i>ADMIN</i></b>
     * @param id Identificador chave primária do usuário.
     * @param userDto Dados do usuário.
     * @param request Informações sobre a requisição HTTP.
     * @return Resposta padrão.
     */
    @PutMapping("/{id}")
	public ResponseEntity<Response> updateUser(
		@PathVariable("id") UUID id,
		@RequestBody @Valid UserDto userDto,
		HttpServletRequest request
	) {
		User user = userService.updateUser(id, userDto);
		logService.saveLog(request, "User updated: " + user.getUsername());
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("User updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
    
    
    /**
     * Atualizar as credenciais de acesso do usuário. Neste caso, qualquer usuário
     * tem acesso à funcionalidade, podendo alterar as próprias credenciais de 
     * acesso.
     * <br><br>
	 * Nível de Acesso: <b><i>USER</i></b>
     * @param id Indentificador chave primária do usuário.
     * @param userDto Dados do usuário.
     * @param request Informações sobre a requisição HTTP.
     * @return Resposta padrão.
     */
    @PutMapping("/credentials/{id}")
	public ResponseEntity<Response> updateCredentials(
		@PathVariable("id") UUID id,
		@RequestBody @Valid UserDto userDto,
		HttpServletRequest request
	) {
		User user = userService.updateUser(id, userDto);
		logService.saveLog(request, "User updated: " + user.getUsername());
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("User updated successfully.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
    
    
    @PutMapping("/password/{id}/{oldpassword}/{newpassword}")
	public ResponseEntity<Response> updatePassword(
		@PathVariable("id") UUID id,
		@PathVariable("oldpassword") String oldPassword,
		@PathVariable("newpassword") String newPassword,
		HttpServletRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
    
    
    @PutMapping("/username/{id}/{newusername}")
	public ResponseEntity<Response> updateUserName(
		@PathVariable("id") UUID id,
		@PathVariable("newusername") String newUserName,
		HttpServletRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
    
    
    @DeleteMapping(value = "/{id}/{delete}")
	public ResponseEntity<Response> deleteUser(
		@PathVariable("id") UUID id,
		@PathVariable("delete") boolean delete,
		HttpServletRequest request
	) {
		Response resp = new Response();
		resp.setId(id.toString());
		User user = null;
		if (delete) {
			user = userService.deleteUser(id);
			logService.saveLog(request, "User deleted: " + user.getUsername());
		    resp.setMessage("User deleted successfully.");
		} else {
			user = userService.undeleteUser(id);
			logService.saveLog(request, "User undeleted: " + user.getUsername());
			resp.setMessage("User undeleted successfully.");
		}
		resp.setTime(LocalDateTime.now());
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
    
    
    @GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
	}
    
    
    @GetMapping(value = "/{id}")
	public ResponseEntity<User> getUser(
		@PathVariable("id") UUID id
	) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
	}
    
    
}
