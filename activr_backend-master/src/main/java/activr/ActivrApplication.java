package activr;

import activr.model.*;
import activr.repository.AccountRepository;
import activr.repository.InterestRepository;
import activr.repository.RoleRepository;
import activr.services.MatchingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.*;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class ActivrApplication {


     //This method should maybe be commented when running while connected to database. maybe...
    @Bean
    CommandLineRunner init(AccountRepository accountRepository,
                           InterestRepository interestRepository,
                           RoleRepository roleRepository) {


        Role role = roleRepository.save(new Role("USER"));
        Set<Role> userRoles= new HashSet<>();
        userRoles.add(role);
        return (evt) -> Arrays.asList(
                "bartholomew,Dr. Morris,turkey,amonkey,rudolph,nmaki,Scooby,garth".split(","))
                .forEach(
                        a -> {
                            Account account = accountRepository.save(new Account(a,
                                    "password",a, "Vader", Gender.MALE, "555-555-5555", userRoles));
                            interestRepository.save(new Interest(account,
                                    Activity.RACQUETBALL, 5, true));
                            interestRepository.save(new Interest(account,
                                    Activity.HANDBALL, 3, false));

                            System.out.println("Attempting to add: userId:" + a);
                        });
    }

    @Bean
    public MatchingService matchingService() {
        return new MatchingService();
    }



    public static void main(String[] args) {
        SpringApplication.run(ActivrApplication.class, args);
    }
}
