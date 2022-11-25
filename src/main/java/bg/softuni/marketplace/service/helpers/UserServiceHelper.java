package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.service.UserService;

import jakarta.validation.constraints.NotNull;

/**
 * {@link UserService} helper methods interface.
 */
public interface UserServiceHelper {

    /**
     * Maps {@link User} to {@link UserViewModel}.
     *
     * @param user the user
     * @return the user view model
     */
    UserViewModel mapUserToViewModel(@NotNull User user);

    /**
     * Update role for user.
     *
     * @param user      the user
     * @param authority the authority
     */
    void updateRoleForUser(@NotNull User user,
                           @NotNull Authority authority);

    /**
     * Gets {@link User} from {@link UserRegisterBindingModel} model.
     *
     * @param bindingModel the {@link UserRegisterBindingModel} model
     * @param isRoot       is this user ROOT (the first user)
     * @return the {@link User} from model
     */
    User getUserFromModel(@NotNull UserRegisterBindingModel bindingModel,
                          boolean isRoot);

    /**
     * Gets default {@link Authority} for new user.
     *
     * @param isRoot the user is root
     * @return the {@link Authority} for user
     */
    Authority getAuthorityForUser(boolean isRoot);

    /**
     * Instantiates a profile for {@link User}.
     *
     * @param user  {@link User}
     * @param email user email
     * @return {@link Profile} for the user
     */
    Profile getUserProfile(@NotNull User user,
                           @ValidUserEmail String email);
}
