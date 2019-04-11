package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.role.RoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.repository.UserRepository;
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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Log
@Service
@Validated
@Transactional
public class UserServiceImpl extends BaseService<User, UUID, UserRepository> implements UserService {

    private static final String USERS = "usersCache";
    private static final String ALL_USERS = "allUsersCache";

    private static final UsernameNotFoundException USERNAME_NOT_FOUND_EXCEPTION =
            new UsernameNotFoundException("Username not found");

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
                           ModelMapper mapper,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = USERS, key = "#username")
    public UserDetails loadUserByUsername(String username) {
        return repository
                .findUserEager(username)
                .orElseThrow(() -> USERNAME_NOT_FOUND_EXCEPTION);
    }

    @Override
    @CacheEvict(cacheNames = ALL_USERS, allEntries = true)
    public void registerUser(@NotNull @Valid UserRegisterBindingModel bindingModel) {
        if (repository.countByEmail(bindingModel.getEmail()) > 0) {
            throw new IllegalArgumentException("Email already used: " + bindingModel.getEmail());
        }

        if (repository.countByUsername(bindingModel.getUsername()) > 0) {
            throw new IllegalArgumentException("Username already used: " + bindingModel.getUsername());
        }

        UserBindingModel user = new UserBindingModel(
                bindingModel.getUsername(),
                bindingModel.getEmail(),
                passwordEncoder.encode(bindingModel.getPassword()));

        user.getAuthorities().addAll(getRolesForUser());

        create(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ALL_USERS, sync = true)
    public List<UserViewModel> allUsers() {
        return repository
                .findAll()
                .stream()
                .map(this::mapUserToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS, allEntries = true),
            @CacheEvict(cacheNames = USERS, key = "#userRoleBindingModel.username")})
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
                .getRolesForAuthority(userRoleBindingModel.getAuthority(), RoleBindingModel.class)
                .stream()
                .map(roleBindingModel -> mapper.map(roleBindingModel, Role.class))
                .collect(Collectors.toList());

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

    private List<RoleBindingModel> getRolesForUser() {
        if (repository.count() == 0L) {
            return roleService.findAll(RoleBindingModel.class);
        } else {
            return List.of(roleService
                    .findByAuthority(Authority.USER, RoleBindingModel.class)
                    .orElseThrow());
        }
    }
}
