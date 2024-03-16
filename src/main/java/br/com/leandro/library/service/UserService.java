package br.com.leandro.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {
	
	
    @Autowired
    private TokenService tokenService;
    
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
			"User not found."
		);
    	User user = userO.get();
    	String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
    	user.setPassword(encryptedPassword);
    	user.setUserName(userDto.userName());
    	return userRepository.save(user);
    }
    
    
    public User updateCredentials(UUID id, UserDto userDto, HttpServletRequest request) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"User not found."
		);
    	User user = userO.get();
		String token = tokenService.recoverToken(request);
        if(token != null){
            String userId = tokenService.validateToken(token);
            UserDetails userDetails = userRepository.findByUserName(userId);
            if (userDetails != null) {
            	if (user == (User)userDetails) {
	            	String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
	            	user.setPassword(encryptedPassword);
	            	user.setUserName(userDto.userName());
            	} else {
            		throw new ResourceNotFoundException(
        				id.toString(),
        				"Forbidden. I"
    				);
            	}
            }
        }
    	return userRepository.save(user);
    }
    
    
    public User deleteUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"User not found."
		);
    	User user = userO.get();
    	if (user.getRole() != Role.ADMIN) {
	    	user.setEnabled(false);
	    	userRepository.save(user);
    	} else {
    		throw new UserRegistrationException(
				user.getId().toString(),
    			"It is not possible to delete an administrator user."
			);
    	}
    	return user;
    }
    
    
    public User undeleteUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"User not found."
		);
    	User user = userO.get();
    	user.setEnabled(true);
    	userRepository.save(user);
    	return user;
    }
    
    
    public List<User> getAllUsers() {
    	List<User> usersList = userRepository.findAll();
    	usersList.forEach(user -> user.setPassword(null));
    	return usersList;
    }
    
    
    public User getUser(UUID id) {
    	Optional<User> userO = userRepository.findById(id);
    	if (userO.isEmpty()) throw new ResourceNotFoundException(
			id.toString(),
			"User not found."
		);
    	User user = userO.get();
    	user.setPassword(null);
    	return user;
    }
    
    
    public User getUser(String userName) {
    	User user = (User) userRepository.findByUserName(userName);
    	if (user == null) throw new ResourceNotFoundException(
			userName,
			"User not found."
		);
    	return user;
    }
    
    
}