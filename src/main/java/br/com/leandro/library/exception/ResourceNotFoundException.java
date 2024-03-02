package br.com.leandro.library.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	private String id;

	
	public ResourceNotFoundException(String id, String message) {
		super(message);
		this.id = id;
	}

	
	public String getId() {
		return id;
	}
	
	
}