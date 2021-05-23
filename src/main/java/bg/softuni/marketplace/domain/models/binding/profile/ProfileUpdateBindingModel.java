package bg.softuni.marketplace.domain.models.binding.profile;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_PROFILE_EMAIL_USED;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_PROFILE_EMAIL_UNIQUE_OR_SAME;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@SpELAssert(message = "{" + CODE_PROFILE_EMAIL_USED + "}",
        groups = GroupOne.class, value = EXPR_PROFILE_EMAIL_UNIQUE_OR_SAME)
public class ProfileUpdateBindingModel implements Bindable<Profile>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ValidId
    private UUID id;

    @ValidUserEmail
    private String email;
}
