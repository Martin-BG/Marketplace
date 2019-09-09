package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthority;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.domain.validation.groups.GroupTwo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class UserRoleBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String ROOT_EXPR = "T(bg.softuni.marketplace.domain.enums.Authority).ROOT";

    private static final String ID_VALIDATION = "@userRepository.existsByIdAndAuthorityNot(#this, " + ROOT_EXPR + ")";
    private static final String AUTHORITY_VALIDATION = "#this ne " + ROOT_EXPR;

    @ValidId
    @SpELAssert(message = "{user.update-role.invalid-user}", groups = GroupTwo.class, value = ID_VALIDATION)
    private UUID id;

    @ValidUserAuthority
    @SpELAssert(message = "{user.update-role.root-authority}", groups = GroupOne.class, value = AUTHORITY_VALIDATION)
    private Authority authority;
}
