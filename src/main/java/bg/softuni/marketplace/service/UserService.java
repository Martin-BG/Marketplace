package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService extends UserDetailsService {

    void registerUser(@NotNull @Valid UserRegisterBindingModel bindingModel);

    List<UserViewModel> allUsers();

    void updateRole(@NotNull @Valid UserRoleBindingModel userRoleBindingModel, @NotNull String principalName);
}
