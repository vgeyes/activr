package activr.repository;

import activr.model.Account;
import activr.model.Activity;
import activr.model.Matched;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nic on 4/30/2015.
 */
public interface MatchRepository extends JpaRepository<Matched, Long> {

    List<Matched> findByAccount1(Account account);

    List<Matched> findByAccount1AndAccount2AndActivity(Account account1, Account accoutn2, Activity activity);

    List<Matched> findByAccount1AndAccepted(Account account, Boolean accepted);
}
