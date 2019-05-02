package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class UserServiceHelperImpl implements UserServiceHelper {

    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserViewModel mapUserToViewModel(User user) {
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

    public void updateRoleForUser(User user, Authority authority) {
        List<Role> rolesForAuthority = roleService
                .getRolesForAuthority(authority, Role.class);

        user.getAuthorities()
                .retainAll(rolesForAuthority);

        user.getAuthorities()
                .addAll(rolesForAuthority);
    }

    public User getUserFromModel(UserRegisterBindingModel bindingModel, boolean isRoot) {
        return new User(
                bindingModel.getUsername(),
                passwordEncoder.encode(bindingModel.getPassword()),
                bindingModel.getEmail(),
                getRolesForUser(isRoot));
    }

    public List<Role> getRolesForUser(boolean isRoot) {
        if (isRoot) {
            return roleService.findAll(Role.class);
        } else {
            return List.of(roleService
                    .findByAuthority(Authority.USER, Role.class)
                    .orElseThrow());
        }
    }
}
