package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import basics.*;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	Optional<Loan> findByBook(Book book);
}
