package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.domain.models.binding.user.UserRoleBindingModel;
import bg.softuni.marketplace.domain.models.view.user.UserViewModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Layout
@Controller
@RequestMapping(WebConfig.URL_USER_ALL)
public class AllUserController extends BaseController {

    public static final String USERS = "users";

    private static final String VIEW_ALL = "user/all";

    private final UserService service;

    public AllUserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String get(Model model) {
        List<UserViewModel> users = service.allUsers();

        model.addAttribute(USERS, users);

        return VIEW_ALL;
    }

    @PatchMapping
    public String patch(@ModelAttribute UserRoleBindingModel userRoleBindingModel, Principal principal) {
        service.updateRole(userRoleBindingModel, principal.getName());

        return redirect(WebConfig.URL_USER_ALL);
    }
}
