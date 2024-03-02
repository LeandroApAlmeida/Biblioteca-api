package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.leandro.library.dto.UserDto;
import br.com.leandro.library.exception.ResourceNotFoundException;
import br.com.leandro.library.exception.UserRegistrationException;
import br.com.leandro.library.model.User;
import br.com.leandro.library.model.User.Role;
import br.com.leandro.library.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName);
    }
    
    
    public User saveUser(UserDto userDto) {
    	User user = (User) loadUserByUsername(userDto.userName());
    	if(user != null) throw new UserRegistrationException(
			user.getId().toString(),
			"The user has already been registered."
		);
        String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
        User newUser = new User(userDto.userName(), encryptedPassword, Role.USER);
        return userRepository.save(newUser);
    }
    
    
    public User updateUser(UUID id, UserDto userDto) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Usuário não encontrado"
		);
    	User user = userO.get();
    	String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
    	user.setPassword(encryptedPassword);
    	user.setUserName(userDto.userName());
    	return userRepository.save(user);
    }
    
    
    public void deleteUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Usuário não encontrado"
		);
    	User user = userO.get();
    	if (user.getRole() != Role.ADMIN) {
	    	user.setEnabled(false);
	    	userRepository.save(user);
    	} else {
    		throw new UserRegistrationException(
				user.getId().toString(),
    			"Não é possível excluir usuário administrador."
			);
    	}
    }
    
    
    public void undeleteUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Usuário não encontrado"
		);
    	User user = userO.get();
    	user.setEnabled(true);
    	userRepository.save(user);
    }
    
    
    public List<User> getAllUsers() {
    	return userRepository.findAll();
    }
    
    
    public User getUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"Usuário não encontrado"
		);
    	return userO.get();
    }
    
    
}