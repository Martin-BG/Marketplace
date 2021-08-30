package bg.softuni.marketplace.service.helpers;

import bg.softuni.marketplace.domain.api.Viewable;

import javax.validation.constraints.NotNull;
import java.util.Collection;

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
     * Maps entities' {@link Collection} to unmodifiable {@link Collection}
     * of elements of the specified view model class.
     *
     * @param <E>            the entity type parameter
     * @param <V>            the view model type parameter
     * @param entities       the entities {@link Collection}
     * @param viewModelClass the view model class
     * @return unmodifiable {@link Collection} of elements of the specified view model class
     */
    <E, V extends Viewable<? extends E>>
    Collection<V> map(@NotNull Collection<? extends E> entities,
                      @NotNull Class<V> viewModelClass);
}
