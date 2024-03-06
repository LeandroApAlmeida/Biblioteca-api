package br.com.leandro.library.system;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.leandro.library.config.Context;

@Component
public class ServerPaths {

	
	@Value("${library.path.media-directory}")
    private String mediaDirectory;
	
	@Value("${library.path.context}")
	private Context context;
	
	
	public String getMediaDirectory() {
		String directoryPath;
		if (context == Context.Development) {
			directoryPath = mediaDirectory;
		} else {
			directoryPath = "\\Library\\Media\\";
		}
		File directory = new File(directoryPath);
		if (!directory.exists()) directory.mkdirs();
		return directoryPath;
	}
	
	
	public File getMediaFile(String fileName) {
		return  new File(getMediaDirectory() + fileName);
	}
	
	
}
