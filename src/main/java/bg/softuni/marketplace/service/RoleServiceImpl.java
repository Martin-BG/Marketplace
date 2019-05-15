package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.repository.RoleRepository;
import bg.softuni.marketplace.service.helpers.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service
@Validated
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final String ROLES_ALL_CACHE = "rolesAllCache";
    private static final String ROLES_BY_AUTHORITY_CACHE = "rolesByAuthorityCache";
    private static final String ROLES_FOR_AUTHORITY_CACHE = "rolesForAuthorityCache";

    private final RoleRepository repository;
    private final Mapper mapper;

    @PostConstruct
    @Transactional
    @CacheEvict(cacheNames = {ROLES_ALL_CACHE, ROLES_BY_AUTHORITY_CACHE, ROLES_FOR_AUTHORITY_CACHE}, allEntries = true)
    public void initRoles() {
        if (repository.count() == 0L) {
            Set<Role> roles = Arrays
                    .stream(Authority.values())
                    .map(Role::new)
                    .collect(Collectors.toSet());

            repository.saveAll(roles);

            log.log(Level.INFO, "Roles created: " +
                    roles.stream().map(Role::getAuthority).collect(Collectors.joining(", ")));
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ROLES_BY_AUTHORITY_CACHE, key = "#authority.name() + #viewModelClass.name")
    public <V extends Viewable<? extends Role>>
    Optional<V> findByAuthority(@NotNull Authority authority,
                                @NotNull Class<V> viewModelClass) {
        return repository
                .findRoleByAuthority(authority)
                .map(role -> mapper.map(role, viewModelClass));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ROLES_FOR_AUTHORITY_CACHE, key = "#authority.name() + #viewModelClass.name")
    public <V extends Viewable<? extends Role>>
    List<V> getRolesForAuthority(@NotNull Authority authority,
                                 @NotNull Class<V> viewModelClass) {
        List<Role> roles = repository.findAll();

        roles.removeIf(role -> role.getAuthority().equals(Authority.Role.ROOT));

        switch (authority) {
        case TRADER:
            roles.removeIf(role -> role.getAuthority().equals(Authority.Role.ADMIN));
            break;
        case USER:
            roles.removeIf(role -> !role.getAuthority().equals(Authority.Role.USER));
            break;
        default:
            break;
        }

        return mapper.map(roles, viewModelClass);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ROLES_ALL_CACHE, key = "#viewModelClass.name")
    public <V extends Viewable<? extends Role>> List<V> findAll(@NotNull Class<V> viewModelClass) {
        List<Role> roles = repository.findAll();

        return mapper.map(roles, viewModelClass);
    }
}
