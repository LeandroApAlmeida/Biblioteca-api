package br.com.leandro.library.controller;

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

import br.com.leandro.library.dto.LoginDto;
import br.com.leandro.library.dto.UserDto;
import br.com.leandro.library.model.User;
import br.com.leandro.library.response.LoginResponse;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.TokenService;
import br.com.leandro.library.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserService userService;
    
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginDto loginDto){
    	UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
			loginDto.userName(),
			loginDto.password()
		);
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) authentication.getPrincipal());
    	User user = (User) userService.loadUserByUsername(loginDto.userName());
    	LoginResponse resp = new LoginResponse();
		resp.setId(user.getId().toString());
		resp.setToken(token);
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
    
    
    @PostMapping
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserDto userDto){
    	User user = userService.saveUser(userDto);
    	Response resp = new Response();
		resp.setId(user.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Cadastrado com sucesso");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
    
    
    @PutMapping("/{id}")
	public ResponseEntity<Response> updateUser(
		@PathVariable("id") UUID id,
		@RequestBody @Valid UserDto userDto
	) {
		userService.updateUser(id, userDto);
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Atualizado com sucesso");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
    
    
    @DeleteMapping(value = "/{id}/{delete}")
	public ResponseEntity<Response> deleteUser(
		@PathVariable("id") UUID id,
		@PathVariable("delete") boolean delete
	) {
		Response resp = new Response();
		resp.setId(id.toString());
		if (delete) {
			userService.deleteUser(id);
		    resp.setMessage("Excluído com sucesso");
		} else {
			userService.undeleteUser(id);
			resp.setMessage("Retornado com sucesso");
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
	public ResponseEntity<User> getBook(@PathVariable("id") UUID id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
	}
    
    
}
