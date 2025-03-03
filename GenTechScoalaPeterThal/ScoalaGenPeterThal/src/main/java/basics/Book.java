package basics;

import java.util.List;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "{book.title.required}")
	@Size(min = 3, max = 255, message = "{book.title.size}")
	private String title;

	@NotEmpty(message = "{book.author.required}")
	private String author;

	@NotNull(message = "{book.publicationYear.required}")
	private Integer publicationYear;

	@ManyToOne
	@NotNull(message = "{book.genre.required}")
	private Genre genre;
	
	@Column(nullable = false)
	private String bookStatus;
	public Long getId() {
		return id;
	}
	
	public String getBookStatus() {
		return bookStatus;
	}
	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	@PrePersist
    public void setDefaultStatus() {
        if (bookStatus == null) {
            bookStatus = "Disponibil"; 
        }
    }
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	


}