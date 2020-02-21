package bg.softuni.marketplace.aspects;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Helper {

    private Helper() {
    }

    /**
     * Get argument values, using supplied SpEL expressions (ex. "#user.username")
     *
     * @param args           method arguments
     * @param parameterNames method parameter names
     * @param expressions    required arguments values as SpEL
     * @return {@link Object}[] with argument values
     */
    public static Object[] getArguments(Object[] args, String[] parameterNames, String[] expressions) {
        Object[] arguments = new Object[expressions.length];

        for (int i = 0; i < expressions.length; i++) {
            arguments[i] = getArgumentValue(args, parameterNames, expressions[i]);
        }

        return arguments;
    }

    /**
     * Inspired by Richa-b's
     * <a href="https://github.com/Richa-b/custom-annotation-with-dynamic-values-using-aop">
     * Getting Dynamic Values From method Parameters in Custom Annotations using Spring AOP</a>
     */
    private static Object getArgumentValue(Object[] args, String[] parameterNames, String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser
                .parseExpression(expression)
                .getValue(context, Object.class);
    }

    /**
     * Parse a SPeL expression from method arguments to a single value which is retuned.
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
        Map<String, Object> variables = IntStream.range(0, args.length).boxed()
                .collect(Collectors.toMap(i -> parameterNames[i], i -> args[i]));
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);

        T value = new SpelExpressionParser()
                .parseExpression(expression)
                .getValue(context, valueClass);

        return value;
    }
}
