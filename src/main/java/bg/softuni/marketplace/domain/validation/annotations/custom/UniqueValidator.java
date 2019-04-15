package bg.softuni.marketplace.domain.validation.annotations.custom;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Validation logic for {@link Unique} annotation.
 * <p>
 * Loads specified {@link Unique#bean} from Spring context and invokes {@link Unique#method} on it.<br>
 * {@link Unique#method} is required to return {@code long} or {@link Long} and to have exactly one
 * argument of type {@link String}:
 * <pre>{@code
 *     long countByUsername(String username);
 *     long countByEmail(String email);
 * }</pre>
 * <p>
 * Returns {@code true} if tested value is {@code null} or if
 * {@link Unique#method} invocation returns {@code 0}.
 *
 * @see Unique
 */

@Log
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private static final String PARAM_NAME = "param";
    private static final String EXPRESSION_FORMAT = "%s(#" + PARAM_NAME + ") == 0L";

    private final ApplicationContext applicationContext;
    private Expression expression;
    private EvaluationContext evaluationContext;

    @Autowired
    public UniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        Object bean = applicationContext.getBean(constraintAnnotation.bean());
        evaluationContext = new StandardEvaluationContext(bean);

        ExpressionParser parser = new SpelExpressionParser();
        String expressionString = String.format(EXPRESSION_FORMAT, constraintAnnotation.method());
        expression = parser.parseExpression(expressionString);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        evaluationContext.setVariable(PARAM_NAME, value);
        return Objects.requireNonNullElse(expression.getValue(evaluationContext, boolean.class), false);
    }
}
