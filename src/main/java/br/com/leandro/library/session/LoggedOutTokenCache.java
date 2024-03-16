package br.com.leandro.library.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.leandro.library.service.TokenService;
import br.com.leandro.library.system.ServerPaths;
import io.jsonwebtoken.lang.Arrays;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.jodah.expiringmap.ExpiringMap;

/**
 * Armazena a lista de tokens ainda não expirados de usuários que
 * fizeram o logout. Isso impede que tokens que foram liberados, porém
 * que ainda não estão expirados sejam usados. Optei por usar a biblioteca
 * ExpiringMap com cache em memódia e não uma solução como redis por ser
 * um servidor muito simples e que terá poucos acessos, dispensando tais
 * soluções mais robustas.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@Component
@Configuration
public class LoggedOutTokenCache {

	
	// Cache em memória RAM.
    private ExpiringMap<String, LocalDateTime> expiringMap;
    
    @Autowired
    private ServerPaths serverPaths;

    
    /**
     * Cria o Map na memória. O Máximo de tokens será limitado a 100.
     */
    public LoggedOutTokenCache() {
        expiringMap = ExpiringMap
		.builder()
        .variableExpiration()
        .maxSize(100)
        .build();
    }

    
    /**
     * Adicionar um novo token ao controle. Feito isso, mesmo que o tokens não
     * tenha expirado, não será possível mais acessar a API com ele.
     * @param token Token a ser adicionado.
     */
    public void addToken(String token) {
        if (!expiringMap.containsKey(token)) {
        	TokenService tokenService = new TokenService();
            Date tokenExpiryDate = tokenService.getTokenExpiry(token);
            long secondAtExpiry = tokenExpiryDate.toInstant().getEpochSecond();
            long secondAtLogout = new Date().toInstant().getEpochSecond();
            long expiryTime = Math.max(0, secondAtExpiry - secondAtLogout);
            expiringMap.put(token, LocalDateTime.now(), expiryTime, TimeUnit.SECONDS);
        }
    }

    
    /**
     * Verificar se um token está incluido.
     * @param token Token de acesso.
     * @return True, se o token está incluído, false, se ele não está incluído.
     */
    public boolean containsToken(String token) {
        return expiringMap.containsKey(token);
    }
    
    
    /**
     * Obter a data que o token foi adicionado.
     * @param token Token de acesso.
     * @return Data que o token foi adicionado.
     */
    public LocalDateTime getAddedTokenTime(String token) {
    	if (containsToken(token)) {
    		return expiringMap.get(token);
    	} else {
    		return null;
    	}
    }
    
    
    /**
     * Grava os tokens do Map num arquivo temporário se o servidor for derrubado.
     */
    @PostConstruct
    private void dumpMapToFile() {
    	try {
        	File file = serverPaths.getCacheFile("expiringMapData.dat");
        	if (file.exists()) {
    	    	try(FileInputStream fis = new FileInputStream(file); 
    			ObjectInputStream ois = new ObjectInputStream(fis)) {
    	    		List<String> keyList = (List<String>) ois.readObject();
    	    		if (keyList != null) {
    	    			for (String key : keyList) {
							addToken(key);
						}
    	    		}
    	    	}
        	}
    	} catch (Exception e) {
		}
    }
    
    
    /**
     * Carrega os tokens salvos no arquivo temporário.
     */
    @PreDestroy
    public void loadMapFromFile() {
    	try {
    		File file = serverPaths.getCacheFile("expiringMapData.dat");
	    	try(FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos)) {
	    		if (expiringMap.size() > 0) {
	    			Object[] keyList = expiringMap.keySet().toArray();
	    			List<Object> list = new ArrayList<Object>(keyList.length);
	    			for (int i = 0; i < keyList.length; i++) {
	    				list.add(keyList[i]);
	    			}
	    			oos.writeObject(list);
	    			fos.flush();
	    		}
	    	}
    	} catch (Exception e) {
		}
    }


}