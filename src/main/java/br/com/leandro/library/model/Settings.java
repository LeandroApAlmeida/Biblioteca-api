package br.com.leandro.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Settings {
	
	@Id
	@Column(name = "setting_key")
	private String key;
	
	@ManyToOne(targetEntity = User.class, optional = false)
	@JoinColumn(name = "id_user")
    private User user;
	
	@Column(name = "value_string", nullable = true)
	private String valueString;
	
	@Column(name = "value_long", nullable = true)
	private long valueLong;
	
	@Column(name = "value_double", nullable = true)
	private double valueDouble;
	
	@Column(name = "value_boolean", nullable = true)
	private boolean valueBoolean;
	
	@Column(name = "value_blob", nullable = true)
    private byte[] valueBlob;

}