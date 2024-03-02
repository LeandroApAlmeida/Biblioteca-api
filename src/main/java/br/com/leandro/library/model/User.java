package br.com.leandro.library.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User implements UserDetails {
	
	
	public enum Role {
		
		ADMIN("ADMIN"),
	    USER("USER");
		
		String role;
		
		private Role(String role) {
			this.role = role;
		}
		
		public String getRole() {
			return role;
		}
		
	}

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "user_name")
	@NotBlank(message = "Nome do usuário deve ser preenchido")
	private String userName;
	
	@Column(name = "password")
	@NotBlank(message = "Senha do usuário deve ser preenchida")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;
	
	@Column(name = "is_enabled")
	private boolean enabled;
	
	
	public User(String userName, String password, Role role){
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(role == Role.ADMIN) {
			return List.of(
				new SimpleGrantedAuthority("ROLE_ADMIN"),
				new SimpleGrantedAuthority("ROLE_USER")
			);
		} else {
			return List.of(
				new SimpleGrantedAuthority("ROLE_USER")
			);
		}
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	
}