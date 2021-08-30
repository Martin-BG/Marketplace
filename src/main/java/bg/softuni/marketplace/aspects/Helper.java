package bg.softuni.marketplace.aspects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Helper {

    /**
     * Get argument values, using supplied SpEL expressions (ex. "#user.username")
     *
     * @param args           method arguments
     * @param parameterNames method parameter names
     * @param expressions    required arguments values as SpEL
     * @return {@link Object}[] with argument values
     */
    public static Object[] getArguments(Object[] args, String[] parameterNames, String[] expressions) {
        return IntStream.range(0, expressions.length).boxed()
                .map(i -> getArgumentValue(args, parameterNames, expressions[i]))
                .toArray();
    }

    /**
     * Inspired by Richa-b's
     * <a href="https://github.com/Richa-b/custom-annotation-with-dynamic-values-using-aop">
     * Getting Dynamic Values From method Parameters in Custom Annotations using Spring AOP</a>
     */
    private static Object getArgumentValue(Object[] args, String[] parameterNames, String expression) {
        Map<String, Object> variables = getParamNameValueMap(parameterNames, args);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);

        ExpressionParser parser = new SpelExpressionParser();

        return parser
                .parseExpression(expression)
                .getValue(context, Object.class);
    }

    private static Map<String, Object> getParamNameValueMap(String[] parameterNames, Object[] args) {
        return IntStream.range(0, args.length).boxed()
                .collect(Collectors.toMap(i -> parameterNames[i], i -> args[i]));
    }

    /**
     * Parse a SpEL expression from method arguments to a single value which is returned.
     *
     * @param args           method arguments
     * @param parameterNames method parameter names
     * @param expression     expression to parse
     * @param valueClass     class of the parsed value
     * @param <T>            Type of the returned parsed value
     * @return Parsed expression.
     */
    public static <T> T getValueFromExpression(Object[] args, String[] parameterNames,
                                               String expression, Class<T> valueClass) {
        Map<String, Object> variables = getParamNameValueMap(parameterNames, args);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);

        ExpressionParser parser = new SpelExpressionParser();

        return parser
                .parseExpression(expression)
                .getValue(context, valueClass);
    }
}
