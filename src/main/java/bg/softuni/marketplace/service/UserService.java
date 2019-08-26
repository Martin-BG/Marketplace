package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserStatusBindingModel;
import bg.softuni.marketplace.domain.models.view.profile.ProfileViewModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    void registerUser(@NotNull UserRegisterBindingModel bindingModel,
                      @NotNull Errors errors);

    void updateRole(@NotNull UserRoleBindingModel bindingModel,
                    @NotNull Errors errors);

    List<UserViewModel> allUsers();

    void activateUser(@NotNull UserStatusBindingModel bindingModel,
                      @NotNull Errors errors);

    void disableUser(@NotNull UserStatusBindingModel bindingModel,
                     @NotNull Errors errors);

    void deleteUser(@NotNull UserDeleteBindingModel bindingModel,
                    @NotNull Errors errors);

    ProfileViewModel getUserProfile(@NotNull UUID id);
}
