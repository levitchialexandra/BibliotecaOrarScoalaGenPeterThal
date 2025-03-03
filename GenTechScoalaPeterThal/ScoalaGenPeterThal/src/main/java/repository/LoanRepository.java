package repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import basics.*;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	Optional<Loan> findByBook(Book book);

	 @Query("SELECT l FROM Loan l WHERE l.returnDate < :currentDate AND l.returned = false")
    List<Loan> findOverdueLoans(LocalDate currentDate);
}
