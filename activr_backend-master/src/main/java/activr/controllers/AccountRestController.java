package activr.controllers;

import activr.exceptions.ForbiddenException;
import activr.model.Account;
import activr.model.Interest;
import activr.repository.AccountRepository;
import activr.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicholas on 4/29/15.
 */
@RestController
@RequestMapping("/{userId}/account")
public class AccountRestController {

    private final AccountRepository accountRepository;
    private final InterestRepository interestRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> update(@PathVariable String userId, @RequestBody Account account, OAuth2Authentication auth) {
        if(!userId.equals(auth.getName())) {
            throw new ForbiddenException(auth.getName());
        }

        Account accountOld = accountRepository.findByUsername(userId).get();

        //Move the data over
        updateAccount(accountOld, account);

        //Save the account
        accountRepository.save(accountOld);

        //Respond with the current state of the account to keep the front end up to date just in case they forgot what they just told me.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(accountOld.getId()).toUri());
        return new ResponseEntity<>(accountOld, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    Account get(@PathVariable String userId, OAuth2Authentication auth) {
        System.out.println("\nAuth name: " + auth.getName()+ "\n");
        return accountRepository.findByUsername(userId).get();
    }

    @Autowired
    public AccountRestController(AccountRepository accountRepository, InterestRepository interestRepository) {
        this.accountRepository = accountRepository;
        this.interestRepository = interestRepository;
    }

    private void updateAccount(Account a1, Account a2){
        if(a2.getPassword() != null) {
            a1.setPassword(a2.getPassword());
        }
        a1.setF_name(a2.getF_name());
        a1.setL_name(a2.getL_name());
        a1.setGender(a2.getGender());
        a1.setPhoneNumber(a2.getPhoneNumber());

        /*
            JPA is awful and is trying too hard to abstract everything away :)
         */
        //TODO:Discuss keeping this logic in the InterestRestController because this is kinda inefficient. So wrong to be doing this.
        // and maintaining data integrity in bi-directional relations is gross. JPA fought me on the unidirectional relation sadly :(
        // So we are just going clear all interests and rebuild with what front end tells us is the interest set.

        a1.getInterests()
                .stream()
                .forEach(interest -> {
                    interestRepository.delete(interest);
                });

        //temporary set to save the newly created interests in. Definitely need to keep this in its own controller.
        Set<Interest> temp = new HashSet<>();

        //Now we rebuild what we just destroyed
        a2.getInterests()
                .stream()
                .forEach(interest -> {
                    System.out.println("A1: " + a1 + "Activity:" + interest.getActivity() + "Skill: " + interest.getSkill() + "Primary: " + interest.isPrimary());
                    Interest tempInterest = new Interest(a1, interest.getActivity(), interest.getSkill(), interest.isPrimary());
                    temp.add(tempInterest);
                    interestRepository.save(tempInterest);
                });
        a1.setInterests(temp);
    }
}
