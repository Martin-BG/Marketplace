/*
 * The MIT License
 *
 * Copyright 2013-2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package bg.softuni.marketplace.domain.validation.annotations.spel;

import bg.softuni.marketplace.domain.validation.annotations.spel.support.RelaxedBooleanTypeConverterDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static bg.softuni.marketplace.domain.validation.annotations.spel.ReflectionUtils.extractStaticMethods;
import static org.springframework.util.StringUtils.hasText;

/**
 * Constraint validator for {@link SpELAssert} that evaluates Spring Expression (SpEL).
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class SpELAssertValidator implements ConstraintValidator<SpELAssert, Object>, BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(SpELAssertValidator.class);
    private static final TypeConverter TYPE_CONVERTER
            = new RelaxedBooleanTypeConverterDecorator(new StandardTypeConverter());

    private Expression expression;
    private Expression applyIfExpression;
    private List<Method> functions = new LinkedList<>();
    private BeanFactory beanFactory;
    private String[] fields;
    private String message;

    @Override
    public void initialize(SpELAssert constraint) {
        ExpressionParser parser = new SpelExpressionParser();

        expression = parser.parseExpression(constraint.value());
        if (hasText(constraint.applyIf())) {
            applyIfExpression = parser.parseExpression(constraint.applyIf());
        }
        for (Class<?> clazz : constraint.helpers()) {
            functions = extractStaticMethods(clazz);
        }

        message = constraint.message();
        fields = constraint.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        EvaluationContext evalContext = createEvaluationContext(object);

        if (isApplyIfValid(evalContext)) {
            LOG.trace("Evaluating expression {{}} on an object: {}", expression.getExpressionString(), object);
            boolean result = evaluate(expression, evalContext);

            if (!result && fields.length > 0) {
                // Report error to a specified field
                context.disableDefaultConstraintViolation();
                for (String field : fields) {
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(field)
                            .addConstraintViolation();
                }
            }

            return result;
        }
        return true;
    }

    private StandardEvaluationContext createEvaluationContext(Object rootObject) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setRootObject(rootObject);
        context.setTypeConverter(TYPE_CONVERTER);

        if (beanFactory != null) {
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        if (!functions.isEmpty()) {
            for (Method helper : functions) {
                context.registerFunction(helper.getName(), helper);
            }

            if (LOG.isTraceEnabled()) {
                LOG.trace(inspectFunctions(context));
            }
        }

        return context;
    }

    private boolean isApplyIfValid(EvaluationContext context) {
        if (applyIfExpression == null) {
            return true;
        }

        LOG.trace("Evaluating applyIf {{}} on an object: {}", applyIfExpression.getExpressionString(), context);
        return evaluate(applyIfExpression, context);
    }

    private static boolean evaluate(Expression expression, EvaluationContext context) {
        Boolean result = expression.getValue(context, Boolean.class);
        return result != null && result;
    }

    private String inspectFunctions(EvaluationContext context) {
        StringBuilder message = new StringBuilder();
        Set<String> names = new HashSet<>(functions.size());

        message.append("Registered functions: \n");

        for (Method function : functions) {
            names.add(function.getName());
        }
        for (String name : names) {
            Object obj = context.lookupVariable(name);
            if (obj instanceof Method) {
                message.append("     #").append(name).append(" -> ").append(obj).append('\n');
            }
        }
        return message.toString();
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
