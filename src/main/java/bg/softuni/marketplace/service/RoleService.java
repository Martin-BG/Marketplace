package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.enums.Authority;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    <V extends Viewable<? extends Role>>
    Optional<V> findByAuthority(@NotNull Authority authority,
                                @NotNull Class<V> viewModelClass);

    <V extends Viewable<? extends Role>>
    List<V> getRolesForAuthority(@NotNull Authority authority,
                                 @NotNull Class<V> viewModelClass);

    <V extends Viewable<? extends Role>>
    List<V> findAll(@NotNull Class<V> viewModelClass);
}
