package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Validated
@Component
public class UserServiceHelperImpl implements UserServiceHelper {

    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserViewModel mapUserToViewModel(@NotNull User user) {
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

    @Override
    public void updateRoleForUser(@NotNull User user, @NotNull Authority authority) {
        List<Role> rolesForAuthority = roleService
                .getRolesForAuthority(authority, Role.class);

        user.getAuthorities()
                .retainAll(rolesForAuthority);

        user.getAuthorities()
                .addAll(rolesForAuthority);
    }

    @Override
    public User getUserFromModel(@NotNull UserRegisterBindingModel bindingModel, boolean isRoot) {
        return new User(
                bindingModel.getUsername(),
                passwordEncoder.encode(bindingModel.getPassword()),
                getRolesForUser(isRoot),
                isRoot);
    }

    @Override
    public List<Role> getRolesForUser(boolean isRoot) {
        if (isRoot) {
            return roleService.findAll(Role.class);
        } else {
            return List.of(roleService
                    .findByAuthority(Authority.USER, Role.class)
                    .orElseThrow());
        }
    }

    @Override
    public Profile getUserProfile(@NotNull User user,
                                  @ValidUserEmail String email) {
        return new Profile(user, email);
    }
}
