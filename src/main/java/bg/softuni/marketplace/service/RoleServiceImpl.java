package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.repository.RoleRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
@Validated
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final String ROLES_CACHE = "rolesCache";
    private static final String ROLES_FOR_AUTHORITY_CACHE = "rolesForAuthorityCache";

    private final RoleRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public RoleServiceImpl(RoleRepository repository,
                           ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostConstruct
    @Transactional
    @CacheEvict(cacheNames = {ROLES_CACHE, ROLES_FOR_AUTHORITY_CACHE}, allEntries = true)
    public void initRoles() {
        if (repository.count() == 0L) {
            Set<Role> roles = Arrays.stream(Authority.values())
                    .map(Role::new)
                    .collect(Collectors.toSet());
            repository.saveAll(roles);

            log.log(Level.INFO, "Roles created: " +
                    roles.stream().map(Role::getAuthority).collect(Collectors.joining(", ")));
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ROLES_CACHE, key = "#authority")
    public <V extends Viewable<Role>>
    Optional<V> findByAuthority(@NotNull Authority authority, @NotNull Class<V> viewModelClass) {
        return repository
                .findRoleByAuthority(authority)
                .map(role -> mapper.map(role, viewModelClass));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ROLES_FOR_AUTHORITY_CACHE, key = "#authority")
    public <V extends Viewable<Role>>
    List<V> getRolesForAuthority(@NotNull Authority authority, @NotNull Class<V> viewModelClass) {
        List<Role> roles = repository.findAll();

        roles.removeIf(role -> role.getAuthority().equals(Authority.ROOT.asRole()));

        switch (authority) {
        case TRADER:
            roles.removeIf(role -> role.getAuthority().equals(Authority.ADMIN.asRole()));
            break;
        case USER:
            roles.removeIf(role -> !role.getAuthority().equals(Authority.USER.asRole()));
            break;
        default:
            break;
        }

        return roles
                .stream()
                .map(role -> mapper.map(role, viewModelClass))
                .collect(Collectors.toList());
    }

    @Override
    public <V extends Viewable<Role>> List<V> findAll(@NotNull Class<V> viewModelClass) {
        List<Role> roles = repository.findAll();

        return roles
                .stream()
                .map(role -> mapper.map(role, viewModelClass))
                .collect(Collectors.toList());
    }
}
