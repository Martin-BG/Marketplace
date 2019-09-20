package bg.softuni.marketplace.domain.validation.annotations.composite;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageCodes {

    public static final String CODE_USER_USERNAME_BLANK = "user.username.blank";
    public static final String CODE_USER_USERNAME_LENGTH = "user.username.length";
    public static final String CODE_USER_USERNAME_USED = "user.username.used";
    public static final String CODE_USER_EMAIL_INVALID = "user.email.invalid";
    public static final String CODE_USER_EMAIL_LENGTH = "user.email.length";
    public static final String CODE_USER_EMAIL_USED = "user.email.used";
    public static final String CODE_USER_PASSWORD_EMPTY = "user.password.empty";
    public static final String CODE_USER_PASSWORD_LENGTH = "user.password.length";
    public static final String CODE_USER_PASSWORDS_NOT_MATCH = "user.passwords.not-match";
    public static final String CODE_USER_MODIFY_ROOT_AUTHORITY = "user.modify.root-authority";
    public static final String CODE_USER_MODIFY_INVALID_ID = "user.modify.invalid-user";
    public static final String CODE_USER_DELETE_INVALID_ID = "user.delete.invalid-user";
}
