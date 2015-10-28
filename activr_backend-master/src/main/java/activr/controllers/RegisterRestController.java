package activr.controllers;

import activr.exceptions.InvalidUsernameException;
import activr.exceptions.UsernameAlreadyExistsException;
import activr.model.Account;
import activr.model.Registration;
import activr.model.Role;
import activr.repository.AccountRepository;
import activr.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicholas on 4/22/15.
 */

@RestController
@RequestMapping("/register")
public class RegisterRestController {

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> register(@RequestBody Registration registration) {
        this.checkForUsername(registration.getUsername());

        /*
            A++ security right here.
            TODO: Hit x500 api or implement a mail server. mail server would be more work.
         */
        if(!registration.getUsername().matches("\\S+@umn.edu")) {
            throw new InvalidUsernameException("Activr currently only supports @umn.edu emails");
        }

        //TODO: Add check for password sanity
        return roleRepository
                .findRoleByName("USER")
                .map(role -> {
                    Set<Role> userRoles = new HashSet<>();
                    userRoles.add(role);
                    Account account = this.accountRepository.save(new Account(registration.getUsername(), registration.getPassword(), registration.getF_name(), registration.getL_name(), registration.getGender(), registration.getPhoneNumber(), userRoles));

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account.getId()).toUri());
                    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                }).get();
    }

    @Autowired
    RegisterRestController(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    private void checkForUsername(String username) {
        if(this.accountRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }
    }
}
