package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.PersonDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.model.Person;
import br.com.leandro.library.repository.PersonRepository;

@Service
public class PersonService {
	
	
	@Autowired
	private PersonRepository personRepository;
	
	
	public Person savePerson(PersonDto personDto) {
		Person person = new Person();
		BeanUtils.copyProperties(personDto, person);
		return personRepository.save(person);
	}
	
	
	public Person updatePerson(UUID id, PersonDto personDto){
		Optional<Person> personO = personRepository.findById(id);
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Person not found."
		);
		Person person = personO.get();
		BeanUtils.copyProperties(personDto, person);
		return personRepository.save(person);
	}
	
	
	public Person deletePerson(UUID id) {
		Optional<Person> personO = personRepository.findById(id);
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Person not found."
		);
		Person person = personO.get();
		person.setDeleted(true);
		personRepository.save(person);
		return person;
	}
	
	
	public Person undeletePerson(UUID id) {
		Optional<Person> personO = personRepository.findById(id);
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Person not found."
		);
		Person person = personO.get();
		person.setDeleted(false);
		personRepository.save(person);
		return person;
	}
	
	
	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}
	
	
	public Person getPerson(UUID id) {
		Optional<Person> personO = personRepository.findById(id);
		if (personO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Person not found."
		);
		return personO.get();
	}
	
	
}