package bg.softuni.marketplace.service;

import bg.softuni.marketplace.aspects.validate.Validate;
import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.groups.AllGroups;
import bg.softuni.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USERS_CACHE = "usersCache";
    private static final String ALL_USERS_CACHE = "allUsersCache";

    private static final Supplier<UsernameNotFoundException> USERNAME_NOT_FOUND_EXCEPTION =
            () -> new UsernameNotFoundException("Username not found");

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = USERS_CACHE, key = "#username")
    public UserDetails loadUserByUsername(String username) {
        return repository
                .findUserEager(username)
                .orElseThrow(USERNAME_NOT_FOUND_EXCEPTION);
    }

    @Override
    @Validate(returnOnError = true, catchException = true, groups = {AllGroups.class})
    public void registerUser(@NotNull UserRegisterBindingModel bindingModel, @NotNull Errors errors) {
        User user = new User(
                bindingModel.getUsername(),
                passwordEncoder.encode(bindingModel.getPassword()),
                bindingModel.getEmail(),
                getRolesForUser());

        repository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ALL_USERS_CACHE, sync = true)
    public List<UserViewModel> allUsers() {
        return repository
                .findAll()
                .stream()
                .map(this::mapUserToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#userRoleBindingModel.username")})
    public void updateRole(@NotNull @Valid UserRoleBindingModel userRoleBindingModel, @NotNull String principalName) {
        if (principalName.equals(userRoleBindingModel.getUsername())) {
            throw new IllegalArgumentException("Change of own role is not allowed.");
        }

        if (userRoleBindingModel.getAuthority() == Authority.ROOT) {
            throw new IllegalArgumentException("ROOT role cannot be added to users.");
        }

        User user = repository
                .findUserEager(userRoleBindingModel.getUsername())
                .filter(UserServiceImpl::isNotRoot)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found or not allowed for roles edit: " + userRoleBindingModel.getUsername()));

        List<Role> rolesForAuthority = roleService
                .getRolesForAuthority(userRoleBindingModel.getAuthority(), Role.class);

        user.getAuthorities()
                .retainAll(rolesForAuthority);

        user.getAuthorities()
                .addAll(rolesForAuthority);
    }

    private static boolean isNotRoot(User user) {
        return user
                .getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .noneMatch(role -> Authority.ROOT.asRole().equals(role));
    }

    private UserViewModel mapUserToViewModel(User user) {
        UserViewModel viewModel = mapper
                .map(user, UserViewModel.class);

        Authority highestAuthority = user
                .getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .map(Authority::fromRole)
                .filter(Objects::nonNull)
                .sorted()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        viewModel.setAuthority(highestAuthority);

        return viewModel;
    }

    private List<Role> getRolesForUser() {
        if (repository.count() == 0L) {
            return roleService.findAll(Role.class);
        } else {
            return List.of(roleService
                    .findByAuthority(Authority.USER, Role.class)
                    .orElseThrow());
        }
    }
}
