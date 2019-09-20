package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ModifyUserById;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.NotRootAuthority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthority;
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

    @ValidId
    @ModifyUserById(groups = GroupTwo.class)
    private UUID id;

    @ValidUserAuthority
    @NotRootAuthority(groups = GroupOne.class)
    private Authority authority;
}
