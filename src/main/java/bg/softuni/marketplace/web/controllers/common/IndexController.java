package bg.softuni.marketplace.web.controllers.common;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Layout
@Controller
public class IndexController extends BaseController {

    private static final String VIEW_INDEX = "index";

    @GetMapping(WebConfig.URL_INDEX)
    public String index(Principal principal) {
        if (principal != null) {
            return redirect(WebConfig.URL_USER_HOME);
        }
        return VIEW_INDEX;
    }
}
