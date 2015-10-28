package activr.controllers;

import activr.exceptions.ForbiddenException;
import activr.exceptions.UserNotFoundException;
import activr.model.Account;
import activr.model.Interest;
import activr.model.Matched;
import activr.repository.AccountRepository;
import activr.repository.InterestRepository;
import activr.repository.MatchRepository;
import activr.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by nicholas on 4/22/15.
 */

@RestController
@RequestMapping("/{userId}/potentialMatch")
public class PotentialMatchRestController {

    private AccountRepository accountRepository;

    private InterestRepository interestRepository;

    private MatchRepository matchRepository;

    private MatchingService matchingService;

    /*
        This endpoint will build a match and send it along will return a new possible match
     */
    @RequestMapping(method = RequestMethod.GET)
    Matched getPotentialMatch(@PathVariable String userId, OAuth2Authentication auth){
        if(!userId.equals(auth.getName())) {
            throw new ForbiddenException(auth.getName());
        }
        Optional<Account> account = accountRepository.findByUsername(userId);
        if(!account.isPresent()){
            throw new UserNotFoundException(userId);
        }

        Collection<Interest> interests = interestRepository.findByAccountUsername(userId);

        return matchingService.findPotentialMatch(account.get(), interests);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> update(@PathVariable String userId, @RequestBody Matched match, OAuth2Authentication auth) {
        if(!userId.equals(auth.getName())) {
            throw new ForbiddenException(auth.getName());
        }
        Optional<Account> account = accountRepository.findByUsername(userId);
        if(!account.isPresent()){
            throw new UserNotFoundException(userId);
        }

        //We filtered their own account when we sent the match so we need to re-add it.
        match.setAccount1(account.get());

        //Save the match for the user, would be good to check that the front end didn't mess it up
        matchRepository.save(match);

        //Respond with the current state of the account to keep the front end up to date just in case they forgot what they just told me.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(match.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }



    @Autowired
    public PotentialMatchRestController(AccountRepository accountRepository, InterestRepository interestRepository, MatchRepository matchRepository, MatchingService matchingService) {
        this.accountRepository = accountRepository;
        this.interestRepository = interestRepository;
        this.matchRepository = matchRepository;
        this.matchingService = matchingService;
    }

}
