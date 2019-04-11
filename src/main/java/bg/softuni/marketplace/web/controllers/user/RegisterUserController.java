package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.domain.models.binding.user.UserRegisterBindingModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Layout
@Controller
@RequestMapping(WebConfig.URL_USER_REGISTER)
public class RegisterUserController extends BaseController {

    public static final String USER = "user";

    private static final String VIEW_REGISTER = "user/register";

    private final UserService userService;

    public RegisterUserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected List<String> getUnmodifiedTextFieldsList() {
        return List.of("password", "confirmPassword");
    }

    @GetMapping
    @Title("nav.register")
    public String get(Model model) {
        model.addAttribute(USER, new UserRegisterBindingModel());

        return VIEW_REGISTER;
    }

    @PostMapping
    public String post(@Valid @ModelAttribute(USER) UserRegisterBindingModel user,
                       Errors errors) {
        if (errors.hasErrors()) {
            return VIEW_REGISTER;
        }

        userService.registerUser(user);

        return redirect(WebConfig.URL_USER_HOME);
    }
}
