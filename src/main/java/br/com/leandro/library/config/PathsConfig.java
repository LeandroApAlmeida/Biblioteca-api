package br.com.leandro.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configura&ccedil;&atilde;o dos caminhos dos diret&oacute;rios usados pelo servidor.
 * 
 * @since 1.0
 * @author Leandro Ap. de Almeida
 */
@ConfigurationProperties(prefix = "library.path")
public class PathsConfig {
	
	
	/**
	 * Caminho do diret&oacute;rio de arquivos de m&iacute;dia do servidor no contexto
	 * de desenvolvimento.
	 */
	private String mediaDirectory;
	
	/**
	 * Contexto de execu&ccedil;&atilde;o do servidor. Deve ser alterado quando fizer a implanta&ccedil;&atilde;o
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