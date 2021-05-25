package bg.softuni.marketplace.service;

import bg.softuni.marketplace.aspects.validate.Validate;
import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.models.binding.profile.ProfileUpdateBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserStatusBindingModel;
import bg.softuni.marketplace.domain.models.projection.user.UserUsernameProjection;
import bg.softuni.marketplace.domain.models.view.profile.ProfileViewModel;
import bg.softuni.marketplace.domain.models.view.user.UserDetailsModel;
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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service("userService")
@Primary
@Validated
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USER_DETAILS_CACHE = "userDetailsCache";
    private static final String ALL_USERS_CACHE = "allUsersCache";

    private static final String USERNAME_NOT_FOUND = "Username not found: ";
    private static final String ID_NOT_FOUND = "Id not found: ";

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserServiceHelper serviceHelper;
    private final SessionService sessionService;
    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = USER_DETAILS_CACHE, key = "#username")
    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findUserByUsername(username)
                .map(UserDetailsModel::new) //Map entity to DTO
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND + username));
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true)
    public void registerUser(@NotNull UserRegisterBindingModel bindingModel,
                             @NotNull Errors errors) {
        boolean isRoot = userRepository.count() == 0L;
        User user = serviceHelper.getUserFromModel(bindingModel, isRoot);
        userRepository.save(user);

        Profile profile = serviceHelper.getUserProfile(user, bindingModel.getEmail());
        profileRepository.save(profile);
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USER_DETAILS_CACHE, key = "#result")})
    public String updateRole(@NotNull UserRoleBindingModel bindingModel,
                             @NotNull Errors errors) {
        return getUserByIdAndProcess(
                bindingModel.getId(),
                user -> {
                    serviceHelper.updateRoleForUser(user, bindingModel.getAuthority());
                    sessionService.logoutUser(user.getUsername());
                    return user.getUsername();
                });
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USER_DETAILS_CACHE, key = "#result")})
    public String activateUser(@NotNull UserStatusBindingModel bindingModel,
                               @NotNull Errors errors) {
        return getUserByIdAndProcess(
                bindingModel.getId(),
                user -> {
                    user.setActive(true);
                    return user.getUsername();
                });
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USER_DETAILS_CACHE, key = "#result")})
    public String disableUser(@NotNull UserStatusBindingModel bindingModel,
                              @NotNull Errors errors) {
        return getUserByIdAndProcess(
                bindingModel.getId(),
                user -> {
                    user.setActive(false);
                    sessionService.logoutUser(user.getUsername());
                    return user.getUsername();
                });
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USER_DETAILS_CACHE, key = "#result")})
    public String deleteUser(@NotNull UserDeleteBindingModel bindingModel,
                             @NotNull Errors errors) {
        return getUserByIdAndProcess(
                bindingModel.getId(),
                user -> {
                    profileRepository.deleteById(user.getId());
                    userRepository.delete(user);
                    sessionService.logoutUser(user.getUsername());
                    return user.getUsername();
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
    public Optional<String> getUsernameById(@NotNull UUID id) {
        return userRepository
                .findProjectedById(id, UserUsernameProjection.class)
                .map(UserUsernameProjection::getUsername);
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
                .orElseThrow(idNotFoundException(id));
    }

    @Override
    @Validate(returnOnError = true, groups = AllGroups.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = ALL_USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = USER_DETAILS_CACHE, key = "#result")})
    public String updateProfile(@NotNull ProfileUpdateBindingModel bindingModel,
                                @NotNull Errors errors) {
        return profileRepository
                .findById(bindingModel.getId())
                .map(profile -> {
                    profile.setEmail(bindingModel.getEmail());
                    return profile.getUser().getUsername();
                })
                .orElseThrow(idNotFoundException(bindingModel.getId()));
    }

    private String getUserByIdAndProcess(@NotNull UUID id,
                                         Function<? super User, String> function) {
        return userRepository
                .findById(id)
                .map(function)
                .orElseThrow(idNotFoundException(id));
    }

    private static Supplier<RuntimeException> idNotFoundException(@NotNull UUID id) {
        return () -> {
            throw new IdNotFoundException(ID_NOT_FOUND + id);
        };
    }
}
