package activr.services;

import activr.exceptions.NoMatchFoundException;
import activr.model.Account;
import activr.model.Interest;
import activr.model.Matched;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Created by Nic on 5/1/2015.
 */
public class MatchingService {


    public MatchingService() {

    }

    private static final String POTENTIAL_MATCH_QUERY = "SELECT a.* FROM account a JOIN interest i ON a.id = i.account_id " +
            "WHERE i.activity = '%s' " +
            "AND i.account_id <> %d " +
            "AND i.account_id NOT IN (SELECT m.account2_id FROM matched m WHERE m.account1_id = %d AND m.activity = '%s') " +
            "LIMIT 1";

    @PersistenceContext
    private EntityManager entityManager;

    /*
        Service method to find a potential match for the given account and interests. Will try primary interest first.
     */
    @Transactional
    public Matched findPotentialMatch(Account account, Collection<Interest> interest) {

        Account macthedAccount = null;

        Interest primaryInterest = interest.stream().filter(inter -> inter.isPrimary()).findFirst().get();

        if(null == primaryInterest) {
            throw new RuntimeException("Account had no primary interest");
        }

        String finalQuery = String.format(POTENTIAL_MATCH_QUERY, primaryInterest.getActivity().toString(), account.getId(), account.getId(), primaryInterest.getActivity().toString());

        //TODO: remove this, update to using query set parameter and named params
        System.out.println("\n\nQUERY: " + finalQuery);

        Query q = entityManager.createNativeQuery(finalQuery, Account.class);

        try {
            macthedAccount = (Account) q.getSingleResult();
        } catch(NoResultException e) {

        }

        if(null == macthedAccount) {
            throw new NoMatchFoundException(account.getUsername());
        }

        return new Matched(account, macthedAccount, primaryInterest.getActivity(), false);
    }

    @Autowired
    public MatchingService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
