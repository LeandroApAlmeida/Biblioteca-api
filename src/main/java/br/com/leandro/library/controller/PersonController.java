package br.com.leandro.library.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.library.dto.PersonDto;
import br.com.leandro.library.model.Person;
import br.com.leandro.library.response.Response;
import br.com.leandro.library.service.LogService;
import br.com.leandro.library.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private LogService logService;
	
	
	@PostMapping
	public ResponseEntity<Response> savePerson(
		@RequestBody @Valid PersonDto personDto,
		HttpServletRequest request
	) {
		Person person = personService.savePerson(personDto);
		logService.saveLog(request, "Person added: " + person.getName());
		Response resp = new Response();
		resp.setId(person.getId().toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Person successfully registered.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updatePerson(
		@PathVariable("id") UUID id, 
		@RequestBody @Valid PersonDto personDto,
		HttpServletRequest request
	) {
		Person person = personService.updatePerson(id, personDto);
		logService.saveLog(request, "Person updated: " + person.getName());
		Response resp = new Response();
		resp.setId(id.toString());
		resp.setStatus(String.valueOf(HttpStatus.CREATED.value()));
		resp.setMessage("Person successfully updated.");
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	
	@DeleteMapping(value = "/{id}/{delete}")
	public ResponseEntity<Response> deletePerson(
		@PathVariable("id") UUID id,
		@PathVariable("delete") boolean delete,
		HttpServletRequest request
	) {
		Response resp = new Response();
		Person person = null;
		resp.setId(id.toString());
		if (delete) {
			person = personService.deletePerson(id);
			resp.setMessage("Person successfully deleted.");
			logService.saveLog(request, "Person deleted: " + person.getName());
		} else {
			person = personService.undeletePerson(id);
			resp.setMessage("Person successfully undeleted.");
			logService.saveLog(request, "Person undeleted: " + person.getName());
		}
		resp.setStatus(String.valueOf(HttpStatus.OK.value()));
		resp.setTime(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons(){
		return ResponseEntity.status(HttpStatus.OK).body(
			personService.getAllPersons()
		);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Person> getBook(
		@PathVariable("id") UUID id
	) {
		return ResponseEntity.status(HttpStatus.OK).body(
			personService.getPerson(id)
		);
	}
	

}