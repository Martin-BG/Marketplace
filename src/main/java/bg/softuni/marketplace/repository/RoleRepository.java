package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.enums.Authority;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
@Repository
public interface RoleRepository extends GenericRepository<Role, Long> {

    Optional<Role> findRoleByAuthority(@NotNull Authority authority);
}
