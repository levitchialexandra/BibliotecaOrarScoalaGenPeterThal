package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import basics.orar.Materie;

public interface MaterieRepository extends JpaRepository<Materie, Long> { 
}
