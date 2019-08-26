package bg.softuni.marketplace.service;

import bg.softuni.marketplace.aspects.validate.Validate;
import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserStatusBindingModel;
import bg.softuni.marketplace.domain.models.view.profile.ProfileViewModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.groups.AllGroups;
import bg.softuni.marketplace.repository.ProfileRepository;
import bg.softuni.marketplace.repository.UserRepository;
import bg.softuni.marketplace.service.exception.IdNotFoundException;
import bg.softuni.marketplace.service.helpers.Mapper;
import bg.softuni.marketplace.service.helpers.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service("userService")
@Primary
@Validated
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERS_CACHE = "usersCache";
    private static final String ALL_USERS_CACHE = "allUsersCache";

    private static final String USERNAME_NOT_FOUND = "Username not found: ";
    private static final String ID_NOT_FOUND = "Id not found: ";

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserServiceHelper serviceHelper;
    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = USERS_CACHE, key = "#username")
    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND + username));
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true)
    public void registerUser(@NotNull UserRegisterBindingModel bindingModel,
                             @NotNull Errors errors) {
        boolean isRoot = userRepository.count() == 0;
        User user = serviceHelper.getUserFromModel(bindingModel, isRoot);
        userRepository.save(user);

        Profile profile = serviceHelper.getUserProfile(user, bindingModel.getEmail());
        profileRepository.save(profile);
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void updateRole(@NotNull UserRoleBindingModel bindingModel,
                           @NotNull Errors errors) {
        userRepository
                .findUserByUsername(bindingModel.getUsername())
                .ifPresentOrElse(
                        user -> serviceHelper.updateRoleForUser(user, bindingModel.getAuthority()),
                        () -> {
                            throw new UsernameNotFoundException(USERNAME_NOT_FOUND + bindingModel.getUsername());
                        });
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ALL_USERS_CACHE, sync = true)
    public List<UserViewModel> allUsers() {
        return userRepository
                .findAll(Sort.by(Sort.Order.asc("username")))
                .stream()
                .map(serviceHelper::mapUserToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void activateUser(@NotNull UserStatusBindingModel bindingModel,
                             @NotNull Errors errors) {
        if (userRepository.activateUser(bindingModel.getUsername()) == 0) {
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND + bindingModel.getUsername());
        }
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void disableUser(@NotNull UserStatusBindingModel bindingModel,
                            @NotNull Errors errors) {
        if (userRepository.disableUser(bindingModel.getUsername()) == 0) {
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND + bindingModel.getUsername());
        }
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USERS_CACHE, key = "#bindingModel.username")})
    public void deleteUser(@NotNull UserDeleteBindingModel bindingModel,
                           @NotNull Errors errors) {
        userRepository
                .findUserByUsername(bindingModel.getUsername())
                .ifPresentOrElse(
                        user -> {
                            profileRepository.deleteById(user.getId());
                            userRepository.delete(user);
                        },
                        () -> {
                            throw new UsernameNotFoundException(USERNAME_NOT_FOUND + bindingModel.getUsername());
                        });
    }

    @Override
    public ProfileViewModel getUserProfile(@NotNull UUID id) {
        return profileRepository
                .findById(id)
                .map(profile -> {
                    ProfileViewModel profileViewModel = mapper.map(profile, ProfileViewModel.class);
                    profileViewModel.setUsername(profile.getUser().getUsername());
                    return profileViewModel;
                })
                .orElseThrow(() -> {
                    throw new IdNotFoundException(ID_NOT_FOUND + id);
                });
    }
}
