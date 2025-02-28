package repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import basics.Book;
import basics.Genre;
public interface BookRepository extends JpaRepository<Book, Long> {

	 List<Book> findByTitleContainingAndAuthorContainingAndGenreIdAndPublicationYear(
		        String title, 
		        String author, 
		        Long genreId, 
		        Integer publicationYear
		    );
	List<Book> findByTitleContainingAndAuthorContainingAndGenreAndPublicationYear(
	        String title, 
	        String author, 
	        Genre genre, 
	        Integer publicationYear
	    );
	Page<Book> findAll(Specification<Book> spec, Pageable pageable);
	
	List<Book> findByTitleContainingAndAuthorContainingAndGenreAndPublicationYear(
            String title, String author, Long genre, Integer publicationYear, Sort sort);
	
	@Query("SELECT b FROM Book b WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
		       "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
		       "(:genreId IS NULL OR b.genre.id = :genreId) AND " +
		       "(:publicationYear IS NULL OR CAST(b.publicationYear AS string) LIKE CONCAT('%', :publicationYear, '%')) AND " +
		       "(:searchValue IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :searchValue, '%'))) ")
    List<Book> findBooksByCriteria(String title, String author, Long genreId, Integer publicationYear,
                                   String searchValue, Pageable pageable);
}
