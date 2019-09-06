package bg.softuni.marketplace.web.controllers.admin;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.aspects.onsuccess.OnSuccess;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserStatusBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.SessionService;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
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

    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping
    public String viewUsers(Model model) {
        List<UserViewModel> users = userService.allUsers();
        model.addAttribute(USERS_ATTRIBUTE_NAME, users);

        return VIEW_USERS_ALL;
    }

    @PatchMapping(params = {USERS_PARAM_UPDATE})
    @PreAuthorize("#user.id ne #bindingModel.id " +
            "and #bindingModel.authority ne T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.update-role.success", args = {"#bindingModel.username", "#bindingModel.authority"})
    public String updateRole(@ModelAttribute UserRoleBindingModel bindingModel,
                             @AuthenticationPrincipal User user,
                             Errors errors) {
        userService.updateRole(bindingModel, errors);

        return redirect(URL_ADMIN_USERS);
    }

    @PatchMapping(params = {USERS_PARAM_ACTIVATE})
    @PreAuthorize("principal.username ne #bindingModel.username")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.activate.success", args = "#bindingModel.username")
    public String activateUser(@ModelAttribute UserStatusBindingModel bindingModel,
                               Errors errors) {
        userService.activateUser(bindingModel, errors);

        return redirect(URL_ADMIN_USERS);
    }

    @PatchMapping(params = {USERS_PARAM_DISABLE})
    @PreAuthorize("principal.username ne #bindingModel.username")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.disable.success", args = "#bindingModel.username")
    public String disableUser(@ModelAttribute UserStatusBindingModel bindingModel,
                              Errors errors) {
        userService.disableUser(bindingModel, errors);
        sessionService.logoutUser(bindingModel.getUsername());

        return redirect(URL_ADMIN_USERS);
    }

    @DeleteMapping
    @PreAuthorize("principal.username ne #bindingModel.username " +
            "and #bindingModel.authority ne T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT, catchException = true, alert = ALL)
    @OnSuccess(message = "users.delete-user.success", args = "#bindingModel.username")
    public String deleteUser(@ModelAttribute UserDeleteBindingModel bindingModel,
                             Errors errors) {
        userService.deleteUser(bindingModel, errors);
        sessionService.logoutUser(bindingModel.getUsername());

        return redirect(URL_ADMIN_USERS);
    }
}
