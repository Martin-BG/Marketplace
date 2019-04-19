package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Layout
@Title("nav.users")
@RequiredArgsConstructor
@Controller
@RequestMapping(WebConfig.URL_USER_ALL)
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
    @OnError(view = WebConfig.URL_USER_ALL,
            method = OnError.Method.REDIRECT,
            catchException = true,
            exceptionTypeIgnore = AccessDeniedException.class)
    public String patch(@ModelAttribute UserRoleBindingModel userRoleBindingModel,
                        Errors errors) {
        userService.updateRole(userRoleBindingModel, errors);

        return redirect(WebConfig.URL_USER_ALL);
    }
}
