package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Layout
@Title("nav.login")
@Controller
@RequestMapping(WebConfig.URL_USER_LOGIN)
public class LoginUserController extends BaseController {

    private static final String VIEW_USER_LOGIN = "user/login";

    @Override
    protected List<String> getUnmodifiedTextFieldsList() {
        return List.of("password");
    }

    @GetMapping
    public String get() {
        return VIEW_USER_LOGIN;
    }
}
