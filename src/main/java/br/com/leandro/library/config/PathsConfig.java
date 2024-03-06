package br.com.leandro.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuração dos caminhos dos diretórios usados pelo servidor.
 */
@ConfigurationProperties(prefix = "library.path")
public class PathsConfig {
	
	
	/**
	 * Caminho do diretório de arquivos de mídia do servidor no contexto
	 * de desenvolvimento.
	 */
	private String mediaDirectory;
	
	/**
	 * Contexto de execução do servidor. Deve ser alterado quando fizer a implantação
	 * caso estiver no modo de desenvolvimento.
	 */
	private Context context = Context.Development;
	
	
	public void setMediaDirectory(String mediaDirectory) {
		this.mediaDirectory = mediaDirectory;
	}
	
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	
	public String getMediaDirectory() {
		return mediaDirectory;
	}
	
	
	public Context getContext() {
		return context;
	}
	

}
