package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Identifiable;

/**
 * Base Entity class
 * Defines equals() and hashCode() methods according to best practices by Vlad Mihalcea
 *
 * @see <a href="https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/">
 * The best way to implement equals, hashCode, and toString with JPA and Hibernate</a>
 */
abstract class BaseEntity<I> implements Identifiable<I> {

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return getId() != null && getId().equals(((Identifiable) o).getId());
    }
}
