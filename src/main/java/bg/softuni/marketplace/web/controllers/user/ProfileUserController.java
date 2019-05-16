package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.domain.models.view.profile.ProfileViewModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static bg.softuni.marketplace.aspects.onerror.OnError.Action.REDIRECT;
import static bg.softuni.marketplace.config.WebConfig.URL_ADMIN_USERS;
import static bg.softuni.marketplace.config.WebConfig.URL_USER_PROFILE;

@Layout
@Title("nav.profile")
@RequiredArgsConstructor
@Controller
@RequestMapping(URL_USER_PROFILE)
public class ProfileUserController extends BaseController {

    public static final String USER_PROFILE_ATTRIBUTE_NAME = "profile";

    private static final String USER_PROFILE = "user/profile";

    private final UserService userService;

    @GetMapping("/{username}")
    @PreAuthorize("principal.username eq #username " +
            "or hasRole(T(bg.softuni.marketplace.domain.enums.Authority).ADMIN)")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT,
            catchException = true, exceptionType = UsernameNotFoundException.class,
            message = "user.profile.username-not-found", args = "#username",
            ignoreMissingErrors = true)
    public String viewRegister(@PathVariable @ModelAttribute String username, Model model) {
        ProfileViewModel profile = userService.getProfile(username);
        model.addAttribute(USER_PROFILE_ATTRIBUTE_NAME, profile);

        return USER_PROFILE;
    }
}
