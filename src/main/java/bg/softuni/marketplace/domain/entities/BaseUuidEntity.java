package bg.softuni.marketplace.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;

import java.io.Serial;
import java.util.UUID;

/**
 * {@link UUID} implementation of {@link BaseEntity} abstract class
 * <p>
 * Use space optimized BINARY(16) instead of CHAR/VARCHAR(36) database format
 * <p>
 * Use {@link AccessType#PROPERTY} for id to avoid LazyInitializationException
 * <p>
 * Use private setter to prevent mutability
 * <ul>
 * Useful SQL commands:
 * <li>
 * Select by id:
 * <br>
 * SELECT * FROM table_name where id = UUID_TO_BIN('24abff5d-f3d9-46df-a5ad-361fcbfe045e');
 * <li>
 * List IDs as UUID:
 * <br>
 * SELECT BIN_TO_UUID(id) FROM table_name
 */
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
class BaseUuidEntity extends BaseEntity<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    private UUID id;
}
