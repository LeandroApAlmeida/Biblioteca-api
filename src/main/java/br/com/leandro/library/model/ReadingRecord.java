package br.com.leandro.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "reading_record")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ReadingRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    private UUID id;

	@ManyToOne(targetEntity = Book.class, optional = false)
	@JoinColumn(name = "id_book")
    private Book book;

    @Column(name = "begin_date", nullable = false)
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(name = "reading_completed", nullable = false)
    private boolean readingCompleted;

    @Column(name = "is_deleted")
    private boolean isDeleted;
	
}
