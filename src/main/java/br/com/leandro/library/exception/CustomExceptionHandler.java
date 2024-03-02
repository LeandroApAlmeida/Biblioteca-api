package br.com.leandro.library.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.leandro.library.response.Response;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response handleResourceNotFoundException(ResourceNotFoundException ex) {
		Response resp = new Response();
		resp.setId(ex.getId());
		resp.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
		resp.setMessage(ex.getMessage());
		resp.setTime(LocalDateTime.now());
		return resp;
	}
	
	@ExceptionHandler(RegistrationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Response handleRegistrationException(RegistrationException ex) {
		Response resp = new Response();
		resp.setId(ex.getId());
		resp.setStatus(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
		resp.setMessage(ex.getMessage());
		resp.setTime(LocalDateTime.now());
		return resp;
	}
	
	
	@ExceptionHandler(UserRegistrationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Response handleUserRegistrationException(UserRegistrationException ex) {
		Response resp = new Response();
		resp.setId(ex.getId());
		resp.setStatus(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
		resp.setMessage(ex.getMessage());
		resp.setTime(LocalDateTime.now());
		return resp;
	}
	

}