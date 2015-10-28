package activr.repository;

import activr.model.Activity;
import activr.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Nic on 4/14/2015.
 */
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Collection<Interest> findByAccountUsername(String username);
}
