package activr.security;

import activr.model.Account;
import activr.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by makix310 on 4/24/2015.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public CustomUserDetailsService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);

        System.out.println("\nLooking for username: " + username + "\n");

        if(!account.isPresent()) {
            System.out.println("Could not find username in loadByUserName");
            throw new UsernameNotFoundException(String.format("User %s does not exist", username));
        }

        return new UserRepositoryUserDetails(account.get());
    }

    private final static class UserRepositoryUserDetails extends Account implements UserDetails {

        private static long serialVersionUID = 1L;

        private UserRepositoryUserDetails(Account account) {
            super(account);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return super.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    /*@Override
    public UserDetails loadByUsername(String username) throws UsernameNotFoundException {
        return (username) -> accountRepository
                .findByUsername(username)
                .map(a -> new User(a.getUsername(), a.getPassword(), true, true, true, true,
                        AuthorityUtils.createAuthorityList("ROLE_USER", "write")))
                .orElseThrow(
                        () -> new UsernameNotFoundException("could not find the user '"
                                + username + "'"));
    }*/
}
