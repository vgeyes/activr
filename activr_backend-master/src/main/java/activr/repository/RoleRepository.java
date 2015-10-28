package activr.repository;

import activr.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by makix310 on 4/24/2015.
 *
 * Only making this to add the roles in application on start
 * in embedded DB. Shouldn't need otherwise...
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);
}
