package bg.softuni.marketplace.web.controllers.admin;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.aspects.onsuccess.OnSuccess;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserStatusBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserDetailsModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import bg.softuni.marketplace.web.resolvers.parameters.StringParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bg.softuni.marketplace.aspects.onerror.OnError.Action.REDIRECT;
import static bg.softuni.marketplace.aspects.onerror.OnError.ErrorToAlert.ALL;
import static bg.softuni.marketplace.config.WebConfig.URL_ADMIN_USERS;

@Layout
@Title("nav.users")
@RequiredArgsConstructor
@Controller
@RequestMapping(URL_ADMIN_USERS)
public class UsersController extends BaseController {

    public static final String USERS_ATTRIBUTE_NAME = "users";

    public static final String USERS_PARAM_UPDATE = "update";
    public static final String USERS_PARAM_ACTIVATE = "activate";
    public static final String USERS_PARAM_DISABLE = "disable";

    private static final String VIEW_USERS_ALL = "admin/users";
    private static final String NOT_SELF = "#user.id ne #bindingModel.id";

    private final UserService userService;

    @GetMapping
    public String viewUsers(Model model) {
        List<UserViewModel> users = userService.allUsers();
        model.addAttribute(USERS_ATTRIBUTE_NAME, users);

        return VIEW_USERS_ALL;
    }

    @PatchMapping(params = {USERS_PARAM_UPDATE})
    @PreAuthorize(NOT_SELF)
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.update-role.success", args = {"#username", "#bindingModel.authority"})
    public String updateRole(@ModelAttribute UserRoleBindingModel bindingModel,
                             Errors errors,
                             @AuthenticationPrincipal UserDetailsModel user,
                             StringParameter username) {
        String updatedUserUsername = userService.updateRole(bindingModel, errors);
        username.setValue(updatedUserUsername);

        return redirect(URL_ADMIN_USERS);
    }

    @PatchMapping(params = {USERS_PARAM_ACTIVATE})
    @PreAuthorize(NOT_SELF)
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.activate.success", args = "#username")
    public String activateUser(@ModelAttribute UserStatusBindingModel bindingModel,
                               Errors errors,
                               @AuthenticationPrincipal UserDetailsModel user,
                               StringParameter username) {
        String enabledUserUsername = userService.activateUser(bindingModel, errors);
        username.setValue(enabledUserUsername);

        return redirect(URL_ADMIN_USERS);
    }

    @PatchMapping(params = {USERS_PARAM_DISABLE})
    @PreAuthorize(NOT_SELF)
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.disable.success", args = "#username")
    public String disableUser(@ModelAttribute UserStatusBindingModel bindingModel,
                              Errors errors,
                              @AuthenticationPrincipal UserDetailsModel user,
                              StringParameter username) {
        String disabledUserUsername = userService.disableUser(bindingModel, errors);
        username.setValue(disabledUserUsername);

        return redirect(URL_ADMIN_USERS);
    }

    @DeleteMapping
    @PreAuthorize(NOT_SELF)
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.delete-user.success", args = "#username")
    public String deleteUser(@ModelAttribute UserDeleteBindingModel bindingModel,
                             Errors errors,
                             @AuthenticationPrincipal UserDetailsModel user,
                             StringParameter username) {
        String deletedUserUsername = userService.deleteUser(bindingModel, errors);
        username.setValue(deletedUserUsername);

        return redirect(URL_ADMIN_USERS);
    }
}
