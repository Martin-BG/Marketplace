package bg.softuni.marketplace.domain.validation.annotations.custom;

import lombok.extern.java.Log;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Validation logic for {@link EqualFields} class-type annotation.
 * <p>
 * Returns {@code true} if all specified fields have equal non-null values or are {@code null}.
 * <ul>Throws {@link EqualFieldsValidatorException} in case of:
 * <li>Less than 2 field names given for validation</li>
 * <li>Invalid field name</li>
 * <li>Inaccessible filed</li>
 * </ul>
 * see {@link EqualFields}
 */

@Log
public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {

    private List<String> fieldsToValidate = List.of();
    private boolean inverse;
    private String message;
    private String forField;

    @Override
    public void initialize(EqualFields constraintAnnotation) {
        fieldsToValidate = Arrays
                .stream(constraintAnnotation.value().length > 0 ?
                        constraintAnnotation.value() :
                        constraintAnnotation.fields())
                .collect(Collectors.toUnmodifiableList());
        message = constraintAnnotation.message();
        inverse = constraintAnnotation.inverse();
        forField = constraintAnnotation.forField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (fieldsToValidate.size() <= 1) {
            String errorMessage = MessageFormat
                    .format("[{0}] At least two fields for validation must be specified: {1}",
                            obj.getClass().getName(),
                            String.join(", ", fieldsToValidate));
            throw new EqualFieldsValidatorException(errorMessage);
        }

        if (!forField.isEmpty() && !fieldsToValidate.contains(forField)) {
            String errorMessage = MessageFormat
                    .format("Target error field [{0}] is not present in validated fields list: {1}",
                            forField,
                            String.join(", ", fieldsToValidate));
            throw new EqualFieldsValidatorException(errorMessage);
        }

        List<String> fieldsNotFound = new ArrayList<>(fieldsToValidate);

        List<Object> fieldValues = Arrays
                .stream(obj.getClass().getDeclaredFields())
                .filter(field -> fieldsToValidate.contains(field.getName()))
                .peek(field -> fieldsNotFound.remove(field.getName()))
                .map(field -> getFieldValue(field, obj))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // Not all required for validation fields found in class
        if (!fieldsNotFound.isEmpty()) {
            String errorMessage = MessageFormat
                    .format("[{0}] Field(s) to validate not found: {1}",
                            obj.getClass().getName(),
                            String.join(", ", fieldsNotFound));
            throw new EqualFieldsValidatorException(errorMessage);
        }

        boolean result;

        if (fieldValues.isEmpty()) {
            // All fields have null values -> equal by contract
            result = true;
        } else if (fieldValues.size() != fieldsToValidate.size()) {
            // Mix of null (filtered-out) and non-null values for fields -> not equal by contract
            result = false;
        } else {
            // Check equality of field values
            result = fieldValues
                    .stream()
                    .distinct()
                    .count() == 1L;
        }

        if (inverse) {
            result = !result;
        }

        if (!result) {
            context.disableDefaultConstraintViolation();
            if (!forField.isEmpty()) {
                // Report error to a specified field
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(forField)
                        .addConstraintViolation();
            } else {
                // Report error to all validated fields
                fieldsToValidate.forEach(field -> context
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(field)
                        .addConstraintViolation()
                );
            }
        }

        return result;
    }

    private static Optional<Object> getFieldValue(Field field, Object obj) {
        try {
            field.setAccessible(true);
            return Optional.ofNullable(field.get(obj));
        } catch (IllegalAccessException e) {
            String message = MessageFormat
                    .format("[{0}] Failed to get value of: {1}",
                            obj.getClass().getName(),
                            field.getName());
            throw new EqualFieldsValidatorException(message, e);
        }
    }
}
