package bg.softuni.marketplace.domain.converters;

import bg.softuni.marketplace.domain.enums.Authority;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AuthorityConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        return authority.getId();
    }

    @Override
    public Authority convertToEntityAttribute(String id) {
        return Authority.fromId(id);
    }
}
