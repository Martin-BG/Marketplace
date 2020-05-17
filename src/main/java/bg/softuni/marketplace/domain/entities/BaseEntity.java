package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Identifiable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base Entity class
 * Defines equals() and hashCode() methods according to best practices by Vlad Mihalcea.<br>
 *
 * Implements {@link Serializable} interface for proper de/serialization of id field in child abstract classes.
 *
 * @see <a href="https://stackoverflow.com/questions/893259/should-an-abstract-class-have-a-serialversionuid/893342">
 * serialVersionUID is required on abstract classes implementing Serializable</a>
 *
 * @see <a href="https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/">
 * The best way to implement equals, hashCode, and toString with JPA and Hibernate</a>
 */
abstract class BaseEntity<I> implements Identifiable<I>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (getId() == null || other == null || getClass() != other.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        I otherId = ((Identifiable<I>) other).getId();

        return Objects.equals(getId(), otherId);
    }
}
