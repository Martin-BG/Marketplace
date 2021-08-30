package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.api.Viewable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;

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
    Collection<V> map(@NotNull Collection<? extends E> entities,
                      @NotNull Class<V> viewModelClass) {
        return entities
                .stream()
                .map(e -> map(e, viewModelClass))
                .toList();
    }
}
