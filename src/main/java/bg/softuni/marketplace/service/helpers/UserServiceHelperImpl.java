package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@RequiredArgsConstructor
@Validated
@Component
public class UserServiceHelperImpl implements UserServiceHelper {

    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserViewModel mapUserToViewModel(@NotNull User user) {
        return mapper
                .map(user, UserViewModel.class);
    }

    @Override
    public void updateRoleForUser(@NotNull User user,
                                  @NotNull Authority authority) {
        user.setAuthority(authority);
    }

    @Override
    public User getUserFromModel(@NotNull UserRegisterBindingModel bindingModel,
                                 boolean isRoot) {
        return new User(
                bindingModel.getUsername(),
                passwordEncoder.encode(bindingModel.getPassword()),
                getAuthorityForUser(isRoot),
                isRoot);
    }

    @Override
    public Authority getAuthorityForUser(boolean isRoot) {
        if (isRoot) {
            return Authority.ROOT;
        }

        return Authority.USER;
    }

    @Override
    public Profile getUserProfile(@NotNull User user,
                                  @ValidUserEmail String email) {
        return new Profile(user, email);
    }
}
