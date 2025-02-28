package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import basics.orar.Clasa;
import basics.orar.Clasa.Ciclu;

public interface ClasaRepository extends JpaRepository<Clasa, Long> {
    List<Clasa> findByCiclu(Ciclu ciclu);
}
