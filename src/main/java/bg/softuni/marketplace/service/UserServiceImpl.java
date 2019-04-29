package bg.softuni.marketplace.service;

import bg.softuni.marketplace.aspects.validate.Validate;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.groups.AllGroups;
import bg.softuni.marketplace.repository.UserRepository;
import bg.softuni.marketplace.service.helpers.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service
@Primary
@Validated
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERS_CACHE = "usersCache";
    private static final String ALL_USERS_CACHE = "allUsersCache";

    private static final Supplier<UsernameNotFoundException> USERNAME_NOT_FOUND_EXCEPTION =
            () -> new UsernameNotFoundException("Username not found");

    private final UserRepository repository;
    private final UserServiceHelper serviceHelper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = USERS_CACHE, key = "#username")
    public UserDetails loadUserByUsername(String username) {
        return repository
                .findUserEager(username)
                .orElseThrow(USERNAME_NOT_FOUND_EXCEPTION);
    }

    @Override
    @Validate(returnOnError = true,
            groups = AllGroups.class)
    @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true)
    public void registerUser(@NotNull UserRegisterBindingModel bindingModel,
                             @NotNull Errors errors) {
        boolean isRoot = repository.count() == 0;
        User user = serviceHelper.getUserFromModel(bindingModel, isRoot);
        repository.save(user);
    }

    @Override
    @Validate(returnOnError = true,
            groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void updateRole(@NotNull UserRoleBindingModel bindingModel,
                           @NotNull Errors errors) {
        repository
                .findUserEager(bindingModel.getUsername())
                .ifPresentOrElse(
                        user -> serviceHelper.updateRoleForUser(user, bindingModel.getAuthority()),
                        () -> {
                            throw new UsernameNotFoundException("Username not found: " + bindingModel.getUsername());
                        });
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ALL_USERS_CACHE, sync = true)
    public List<UserViewModel> allUsers() {
        return repository
                .findAll()
                .stream()
                .map(serviceHelper::mapUserToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    @Validate(returnOnError = true,
            groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void deleteUser(@NotNull UserDeleteBindingModel bindingModel,
                           @NotNull Errors errors) {
        if (repository.deleteByUsername(bindingModel.getUsername()) == 0) {
            throw new UsernameNotFoundException("Username not found: " + bindingModel.getUsername());
        }
    }
}
