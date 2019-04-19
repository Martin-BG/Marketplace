package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService extends UserDetailsService {

    void registerUser(@NotNull UserRegisterBindingModel bindingModel,
                      @NotNull Errors errors);

    List<UserViewModel> allUsers();

    void updateRole(@NotNull UserRoleBindingModel userRoleBindingModel,
                    @NotNull Errors errors,
                    @NotNull String principalName);
}
