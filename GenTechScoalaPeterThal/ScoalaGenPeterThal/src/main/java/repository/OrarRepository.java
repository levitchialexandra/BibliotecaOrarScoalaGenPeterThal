package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import basics.orar.Orar;

import java.util.List;


public interface OrarRepository extends JpaRepository<Orar,Long>{
    List<Orar> findByClasaNume(String numeClasa);
    List<Orar> findByClasaNumeAndZiuaAndOraInceput(String numeClasa, String ziua, LocalTime oraInceput);
}
