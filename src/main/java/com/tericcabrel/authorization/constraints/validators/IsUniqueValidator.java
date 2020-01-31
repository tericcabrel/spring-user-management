package com.tericcabrel.authorization.constraints.validators;

import com.tericcabrel.authorization.constraints.IsUnique;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

@Component
public class IsUniqueValidator implements ConstraintValidator <IsUnique, String> {

    private String modelName;
    private String propertyName;

    private final ApplicationContext applicationContext;

    public IsUniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(final IsUnique constraintAnnotation) {
        modelName = constraintAnnotation.model();
        propertyName = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        String repName = "com.tericcabrel.authorization.repositories.mongo." + modelName + "Repository";
        Object result = null;

        try {
            Class<?> type = Class.forName(repName);
            Object instance = this.applicationContext.getBean(repName);

            result = type.getMethod("findBy"+propertyName, String.class).invoke(instance, value);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return result == null;
    }
}
