package repository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import basics.*;
@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);
	
}
