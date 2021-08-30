package bg.softuni.marketplace.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.*;
import java.io.Serial;

/**
 * {@link Long} implementation of {@link BaseEntity} abstract class
 * <p>
 * Use {@link AccessType#PROPERTY} for id to avoid LazyInitializationException
 * <p>
 * Use private setter to prevent mutability
 */

@Setter(AccessLevel.PRIVATE)
@Getter
@MappedSuperclass
abstract class BaseLongEntity extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, insertable = false, updatable = false)
    @Access(AccessType.PROPERTY)
    private Long id;
}
