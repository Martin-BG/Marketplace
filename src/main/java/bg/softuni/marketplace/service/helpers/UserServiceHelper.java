package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;

import java.util.List;

public interface UserServiceHelper {

    UserViewModel mapUserToViewModel(User user);

    void updateRoleForUser(User user, Authority authority);

    User getUserFromModel(UserRegisterBindingModel bindingModel, boolean isRoot);

    List<Role> getRolesForUser(boolean isRoot);

    boolean isRoot(User user);
}
