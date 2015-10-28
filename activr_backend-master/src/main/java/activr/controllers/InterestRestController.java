package activr.controllers;

import activr.exceptions.ForbiddenException;
import activr.exceptions.UserNotFoundException;

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

import java.util.Collection;

/**
 * Created by Nic on 4/14/2015.
 *
 * TODO: implement the rest of the http methods that are necessary, delete for certain.
 */
@RestController
@RequestMapping("/{userId}/interests")
public class InterestRestController {

    private final InterestRepository interestRepository;

    private final AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Interest input, OAuth2Authentication auth) {
        if(!userId.equals(auth.getName())) {
            throw new ForbiddenException(auth.getName());
        }
        return this.accountRepository
                        .findByUsername(userId)
                        .map(account -> {
                            Interest result = interestRepository.save(new Interest(account, input.getActivity(), input.getSkill(), input.isPrimary()));

                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                                    .buildAndExpand(result.getId()).toUri());
                            return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                        }).get();
    }

    @RequestMapping(value = "/{interestId}", method = RequestMethod.GET)
    Interest getInterest(@PathVariable String userId, @PathVariable Long interestId) {
        return this.interestRepository.findOne(interestId);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Interest> getInterests(@PathVariable String userId) {
        return this.interestRepository.findByAccountUsername(userId);
    }

    @Autowired
    InterestRestController(InterestRepository interestkRepository,
                           AccountRepository accountRepository) {
        this.interestRepository = interestkRepository;
        this.accountRepository = accountRepository;
    }
}
