package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.api.Viewable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link Mapper} implementation, using {@link ModelMapper} for mapping.
 */
@RequiredArgsConstructor
@Validated
@Component
public class MapperImpl implements Mapper {

    private final ModelMapper modelMapper;

    public <E, V extends Viewable<? extends E>>
    V map(@NotNull E entity,
          @NotNull Class<V> viewModelClass) {
        return modelMapper
                .map(entity, viewModelClass);
    }

    public <E, V extends Viewable<? extends E>>
    Optional<V> map(@NotNull Optional<? extends E> entityOptional,
                    @NotNull Class<? extends V> viewModelClass) {
        return entityOptional
                .map(e -> map(e, viewModelClass));
    }

    public <E, V extends Viewable<? extends E>>
    List<V> map(@NotNull Collection<? extends E> entities,
                @NotNull Class<? extends V> viewModelClass) {
        return entities
                .stream()
                .map(e -> map(e, viewModelClass))
                .collect(Collectors.toList());
    }
}
