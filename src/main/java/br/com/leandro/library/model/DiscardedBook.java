package br.com.leandro.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "discarded_book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DiscardedBook implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_book")
    private UUID id;
    
    @MapsId
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_book")
    private Book book; 
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "date", nullable = false)
	private LocalDateTime date;
	
	@Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;

}