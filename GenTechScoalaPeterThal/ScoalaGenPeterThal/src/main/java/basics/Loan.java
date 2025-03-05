package basics;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	@Column(nullable = false)
	private LocalDate loanDate;
	@Column(nullable = false)

	private LocalDate returnDate;
	@ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; 
	
	@Column(nullable = false)
	private Boolean returned;
	
	@Column(nullable = false)
    private String loanStatus;
	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public Long getId() {
		return id;
	}
	@PrePersist
    public void setDefaultStatus() {
        if (loanStatus == null) {
            loanStatus = "Disponibil"; 
        }
    }
	public Boolean getReturned() {
		return returned;
	}

	public void setReturned(Boolean rned) {
	returned = rned;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
