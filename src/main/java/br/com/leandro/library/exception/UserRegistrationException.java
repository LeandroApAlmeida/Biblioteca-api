package br.com.leandro.library.exception;

public class UserRegistrationException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	private String id;
	
	
	public UserRegistrationException(String id, String message) {
		super(message);
		this.id = id;
	}
	
	
	public String getId() {
		return id;
	}

	
}