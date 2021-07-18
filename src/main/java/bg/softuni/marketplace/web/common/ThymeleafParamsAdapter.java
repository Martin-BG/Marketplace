package bg.softuni.marketplace.web.common;

import bg.softuni.marketplace.config.InterceptorsConfig;
import bg.softuni.marketplace.config.WebConfig;
import bg.softuni.marketplace.web.controllers.admin.UsersController;
import bg.softuni.marketplace.web.controllers.user.ProfileUserController;
import bg.softuni.marketplace.web.controllers.user.RegisterUserController;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ThymeleafParamsAdapter {

    private final String titleAttributeName = InterceptorsConfig.TITLE_ATTRIBUTE_NAME;
    private final String alertAttributeName = InterceptorsConfig.ALERT_ATTRIBUTE_NAME;
    private final String viewAttributeName = InterceptorsConfig.VIEW_ATTRIBUTE_NAME;

    private final String usersAttributeName = UsersController.USERS_ATTRIBUTE_NAME;
    private final String usersParamUpdate = UsersController.USERS_PARAM_UPDATE;
    private final String usersParamActivate = UsersController.USERS_PARAM_ACTIVATE;
    private final String usersParamDisable = UsersController.USERS_PARAM_DISABLE;

    private final String usersProfileAttributeName = ProfileUserController.USER_PROFILE_ATTRIBUTE_NAME;

    private final String registerUserAttributeName = RegisterUserController.USER_ATTRIBUTE_NAME;

    private final String urlIndex = WebConfig.URL_INDEX;
    private final String urlUserRegister = WebConfig.URL_USER_REGISTER;
    private final String urlUserHome = WebConfig.URL_USER_HOME;
    private final String urlUserProfile = WebConfig.URL_USER_PROFILE;
    private final String urlUserLogin = WebConfig.URL_USER_LOGIN;
    private final String urlUserLogout = WebConfig.URL_USER_LOGOUT;
    private final String urlAdminBase = WebConfig.URL_ADMIN_BASE;
    private final String urlAdminUsers = WebConfig.URL_ADMIN_USERS;
}
