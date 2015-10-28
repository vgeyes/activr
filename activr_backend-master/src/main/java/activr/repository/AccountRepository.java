package activr.repository;

import activr.model.Account;
import activr.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Nic on 4/14/2015.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findById(long id);
}
