package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import basics.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
	List<Genre> findFirstByNameContaining(String name);
}
