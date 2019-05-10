package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.api.Viewable;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface Mapper. Generic methods to map entities to view models.
 */
public interface Mapper {

    /**
     * Maps an entity to the specified view model class.
     *
     * @param <E>            the entity type parameter
     * @param <V>            the view model type parameter
     * @param entity         the entity
     * @param viewModelClass the view model class
     * @return the view model
     */
    <E, V extends Viewable<? extends E>>
    V map(@NotNull E entity,
          @NotNull Class<V> viewModelClass);

    /**
     * Maps an entity {@link Optional} to {@link Optional} of the specified view model class.
     *
     * @param <E>            the entity type parameter
     * @param <V>            the view model type parameter
     * @param entityOptional the entity optional
     * @param viewModelClass the view model class
     * @return the mapped {@link Optional} or {@link Optional#empty()} for empty {@code entityOptional}
     */
    <E, V extends Viewable<? extends E>>
    Optional<V> map(@NotNull Optional<? extends E> entityOptional,
                    @NotNull Class<V> viewModelClass);

    /**
     * Maps entities' {@link Collection} to {@link List} of elements of the specified view model class.
     *
     * @param <E>            the entity type parameter
     * @param <V>            the view model type parameter
     * @param entities       the entities {@link Collection}
     * @param viewModelClass the view model class
     * @return the {@link List} of elements of the specified view model class
     */
    <E, V extends Viewable<? extends E>>
    List<V> map(@NotNull Collection<? extends E> entities,
                @NotNull Class<V> viewModelClass);
}
