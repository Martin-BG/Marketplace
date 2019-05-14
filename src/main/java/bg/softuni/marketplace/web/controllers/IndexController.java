package bg.softuni.marketplace.web.controllers;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Layout
@Controller
public class IndexController extends BaseController {

    private static final String VIEW_INDEX = "index";

    @GetMapping(WebConfig.URL_INDEX)
    @Title("nav.index")
    public String viewIndex(Principal principal) {
        if (principal != null) {
            return redirect(WebConfig.URL_USER_HOME);
        }

        return VIEW_INDEX;
    }
}
