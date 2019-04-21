package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.domain.models.binding.user.UserDeleteBindingModel;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Layout
@Title("nav.users")
@RequiredArgsConstructor
@Controller
@RequestMapping(WebConfig.URL_ADMIN_USERS)
public class AllUserController extends BaseController {

    public static final String USERS_ATTRIBUTE_NAME = "users";

    private static final String VIEW_USERS_ALL = "user/all";

    private final UserService userService;

    @GetMapping
    public String get(Model model) {
        List<UserViewModel> users = userService.allUsers();

        model.addAttribute(USERS_ATTRIBUTE_NAME, users);

        return VIEW_USERS_ALL;
    }

    @PatchMapping
    @PreAuthorize("principal.username ne #bindingModel.username " +
            "and #bindingModel.authority ne T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    @OnError(view = WebConfig.URL_ADMIN_USERS,
            action = OnError.Action.REDIRECT,
            catchException = true)
    public String patch(@ModelAttribute UserRoleBindingModel bindingModel,
                        Errors errors) {
        userService.updateRole(bindingModel, errors);

        return redirect(WebConfig.URL_ADMIN_USERS);
    }


    @DeleteMapping
    @PreAuthorize("principal.username ne #bindingModel.username " +
            "and #bindingModel.authority ne T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    @OnError(view = WebConfig.URL_ADMIN_USERS,
            action = OnError.Action.REDIRECT,
            catchException = true)
    public String patch(@ModelAttribute UserDeleteBindingModel bindingModel,
                        Errors errors) {
        userService.deleteUser(bindingModel, errors);

        return redirect(WebConfig.URL_ADMIN_USERS);
    }
}
