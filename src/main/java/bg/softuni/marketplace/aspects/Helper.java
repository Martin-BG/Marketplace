package bg.softuni.marketplace.aspects;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public final class Helper {

    private Helper() {
    }

    /**
     * Get argument values, using supplied SpEL expressions (ex. "#user.username")
     *
     * @param args        method arguments
     * @param paramNames  method parameter names
     * @param expressions required arguments values as SpEL
     * @return {@link Object}[] with argument values
     */
    public static Object[] getArguments(Object[] args, String[] paramNames, String[] expressions) {
        Object[] arguments = new Object[expressions.length];

        for (int i = 0; i < expressions.length; i++) {
            arguments[i] = getArgumentValue(args, paramNames, expressions[i]);
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
}
