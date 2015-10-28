package activr.controllers;

import activr.exceptions.UserNotFoundException;
import activr.model.Account;
import activr.model.Matched;
import activr.repository.AccountRepository;
import activr.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by nicholas on 4/22/15.
 */
@RestController
@RequestMapping("/{userId}/matches")
public class MatchRestController {

    private MatchRepository matchRepository;
    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.GET)
    List<Matched> getMatch(@PathVariable String userId){
        Account account = accountRepository.findByUsername(userId).get();
        if(null == account) {
            throw new UserNotFoundException(userId);
        }
        return matchRepository.findByAccount1AndAccepted(account,true);
    }

    @Autowired
    public MatchRestController(MatchRepository matchRepository, AccountRepository accountRepository){
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
    }
}
