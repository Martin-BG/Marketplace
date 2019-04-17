package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Layout
@Title("nav.home")
@Controller
@RequestMapping(WebConfig.URL_USER_HOME)
public class HomeUserController {

    private static final String VIEW_HOME = "user/home";

    @GetMapping
    public String get() {
        return VIEW_HOME;
    }
}
