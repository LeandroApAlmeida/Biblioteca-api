package br.com.leandro.library.service;

import java.time.LocalDateTime;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.leandro.library.model.Log;
import br.com.leandro.library.model.User;
import br.com.leandro.library.repository.LogRepository;
import br.com.leandro.library.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogService {
	
	
	@Autowired
	private LogRepository logRepository;
	
	@Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserRepository userRepository;
    
    
    public LogService() {
    	log.atLevel(Level.INFO);
	}

	
	public void saveLog(User user, HttpServletRequest request, String logData) {
		try {
			String ip = request.getRemoteAddr();
			logRepository.save(new Log(
				0,
				user,
				LocalDateTime.now(),
				logData,
				ip
			));
		} catch (Exception ex) {
		}
	}
	
	
	public void saveLog(HttpServletRequest request, String logData) {
		try {
			User user = null;
			String token = tokenService.recoverToken(request);
	        if(token != null){
	            String userId = tokenService.validateToken(token);
	            UserDetails userDetails = userRepository.findByUserName(userId);
	            if (userDetails != null) {
	            	user = (User) userDetails;
	            	saveLog(user, request, logData);
	            }
	        }
		} catch (Exception ex) {
		}
	}
	
	
}