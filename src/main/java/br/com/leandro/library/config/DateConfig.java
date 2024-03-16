package br.com.leandro.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

/**
 * Define o formato de Timestamp como ISO-8601.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@Configuration
public class DateConfig {
	
	
	/**Formato da data no padr&atilde;o ISO-8601.*/
    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    /**Serializer para o formato ISO-8601.*/
    public static LocalDateTimeSerializer SERIALIZER = new LocalDateTimeSerializer(
		DateTimeFormatter.ofPattern(ISO_8601_FORMAT)
	);
    
    
    @Bean
    @Primary
    ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(SERIALIZER);
        return new ObjectMapper().registerModule(module);
    }
    
    
}