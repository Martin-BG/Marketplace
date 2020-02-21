package bg.softuni.marketplace.web.controllers.user;

import bg.softuni.marketplace.aspects.onerror.OnError;
import bg.softuni.marketplace.aspects.onsuccess.OnSuccess;
import bg.softuni.marketplace.domain.models.binding.profile.ProfileUpdateBindingModel;
import bg.softuni.marketplace.domain.models.view.profile.ProfileViewModel;
import bg.softuni.marketplace.domain.models.view.user.UserDetailsModel;
import bg.softuni.marketplace.service.UserService;
import bg.softuni.marketplace.service.exception.IdNotFoundException;
import bg.softuni.marketplace.web.annotations.Layout;
import bg.softuni.marketplace.web.annotations.Title;
import bg.softuni.marketplace.web.controllers.BaseController;
import bg.softuni.marketplace.web.resolvers.parameters.StringParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("#user.id eq #id " +
            "or hasRole(T(bg.softuni.marketplace.domain.enums.Authority).ADMIN)")
    @OnError(view = URL_ADMIN_USERS, action = REDIRECT,
            catchException = true, exceptionType = IdNotFoundException.class,
            message = "user.profile.id-not-found", args = "#id",
            ignoreMissingErrors = true)
    public String viewProfile(@PathVariable @ModelAttribute UUID id,
                              @AuthenticationPrincipal UserDetailsModel user,
                              Model model) {
        ProfileViewModel profile = userService.getUserProfile(id);
        model.addAttribute(USER_PROFILE_ATTRIBUTE_NAME, profile);

        return URL_USER_PROFILE;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#user.id eq #id " +
            "or hasRole(T(bg.softuni.marketplace.domain.enums.Authority).ADMIN)")
    @OnError(view = "T(bg.softuni.marketplace.config.WebConfig).URL_USER_PROFILE.concat('/').concat(#id)",
            action = REDIRECT,
            catchException = true, exceptionType = IdNotFoundException.class,
            message = "user.profile.id-not-found", args = "#id",
            alert = OnError.ErrorToAlert.ALL)
    @OnSuccess(message = "user.profile.update-success", args = {"#username"})
    public String updateProfile(@PathVariable @ModelAttribute UUID id,
                                @ModelAttribute ProfileUpdateBindingModel bindingModel,
                                Errors errors,
                                @AuthenticationPrincipal UserDetailsModel user,
                                StringParameter username) {
        String updatedUserUsername = userService.updateProfile(bindingModel, errors);
        username.setValue(updatedUserUsername);

        return redirect(String.join("/", URL_USER_PROFILE, id.toString()));
    }
}
