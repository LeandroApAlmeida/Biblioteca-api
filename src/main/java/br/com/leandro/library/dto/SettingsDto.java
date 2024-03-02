package br.com.leandro.library.dto;

public record SettingsDto(
		
	String key,

	String valueString,

	long valueLong,

	double valueDouble,

	boolean valueBoolean,

    byte[] blob
    
) {
}