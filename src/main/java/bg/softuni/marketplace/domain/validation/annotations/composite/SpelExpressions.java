package bg.softuni.marketplace.domain.validation.annotations.composite;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpelExpressions {

    public static final String EXPR_PROFILE_EMAIL_UNIQUE = "not @profileRepository.existsByEmail(#this)";
    public static final String EXPR_PROFILE_EMAIL_UNIQUE_OR_SAME =
            "not @profileRepository.existsByEmail(#this.email) " +
                    "or @profileRepository.existsByEmailAndId(#this.email, #this.id)";
    public static final String EXPR_USER_USERNAME_UNIQUE = "not @userRepository.existsByUsername(#this)";

    private static final String ROOT = "T(bg.softuni.marketplace.domain.enums.Authority).ROOT";

    public static final String EXPR_AUTHORITY_NOT_ROOT = "#this ne " + ROOT;

    public static final String EXPR_USER_ID_DELETE =
            "@userRepository.existsByIdAndActiveAndAuthorityNot(#this, false, " + ROOT + ")";

    public static final String EXPR_USER_ID_MODIFY =
            "@userRepository.existsByIdAndAuthorityNot(#this, " + ROOT + ")";
}
