package br.com.leandro.library.session;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;

/**
 * Cookie de sessão do usuário.
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@Component
public class SessionCookie {
	
	
	public Cookie createCookie(String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(7200);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setDomain("localhost");
		return cookie;
	}
	
	
	public Cookie deleteCookie(){
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setDomain("localhost");
        return cookie;
    }

	
}
