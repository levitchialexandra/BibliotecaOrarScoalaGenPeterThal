package repository;
import org.springframework.data.jpa.repository.JpaRepository;

import basics.orar.Orar;

import java.util.List;


public interface OrarRepository extends JpaRepository<Orar,Long>{
    List<Orar> findByClasaNume(String numeClasa);
}
