package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.api.Viewable;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Mapper {

    <E, V extends Viewable<? extends E>>
    V map(@NotNull E entity,
          @NotNull Class<V> viewModelClass);

    <E, V extends Viewable<? extends E>>
    Optional<V> map(@NotNull Optional<? extends E> entityOptional,
                    @NotNull Class<V> viewModelClass);

    <E, V extends Viewable<? extends E>>
    List<V> map(@NotNull Collection<? extends E> entities,
                @NotNull Class<V> viewModelClass);
}
