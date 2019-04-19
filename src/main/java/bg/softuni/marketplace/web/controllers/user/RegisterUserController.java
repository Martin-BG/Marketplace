package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Layout
@Title("nav.register")
@RequiredArgsConstructor
@Controller
@RequestMapping(WebConfig.URL_USER_REGISTER)
public class RegisterUserController extends BaseController {

    public static final String USER_ATTRIBUTE_NAME = "user";

    private static final String VIEW_REGISTER = "user/register";

    private final UserService userService;

    @Override
    protected List<String> getUnmodifiedTextFieldsList() {
        return List.of("password", "confirmPassword");
    }

    @ModelAttribute(USER_ATTRIBUTE_NAME)
    private UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping
    public String get() {
        return VIEW_REGISTER;
    }

    @PostMapping
    @OnError(view = VIEW_REGISTER,
            catchException = true,
            exceptionTypeIgnore = AccessDeniedException.class)
    public String post(@ModelAttribute(USER_ATTRIBUTE_NAME) UserRegisterBindingModel user,
                       Errors errors) {
        userService.registerUser(user, errors);

        return redirect(WebConfig.URL_USER_LOGIN + "?register");
    }
}
