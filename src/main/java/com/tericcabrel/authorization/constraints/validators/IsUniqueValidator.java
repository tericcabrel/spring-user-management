package com.tericcabrel.authorization.constraints.validators;

import com.tericcabrel.authorization.constraints.IsUnique;
import com.tericcabrel.authorization.utils.Helpers;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

@Component
public class IsUniqueValidator implements ConstraintValidator <IsUnique, Object> {

    private String propertyName;
    private String repositoryName;
    private UpdateAction action;

    private final ApplicationContext applicationContext;

    public IsUniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(final IsUnique constraintAnnotation) {
        propertyName = constraintAnnotation.property();
        repositoryName = constraintAnnotation.repository();
        action = constraintAnnotation.action();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        Object result;
        String finalRepositoryName = "com.tericcabrel.authorization.repositories." + repositoryName;

        try {
            Class<?> type = Class.forName(finalRepositoryName);
            Object instance = this.applicationContext.getBean(finalRepositoryName);

            final Object propertyObj = BeanUtils.getProperty(value, propertyName);
            final Object objId = BeanUtils.getProperty(value, "id");

            String finalPropertyName = Helpers.capitalize(propertyName);

            if (propertyObj == null) {
                return true;
            }

            result = type.getMethod("findBy"+finalPropertyName, String.class).invoke(instance, propertyObj.toString());

            if(action == UpdateAction.INSERT) {
                return result == null;
            } else {
                Class<?> resultType = result.getClass();
                String resultId = resultType.getMethod("getId").invoke(result).toString();

                return resultId == objId;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

            return false;
        }
    }

    public static enum UpdateAction {
        INSERT,
        UPDATE
    }
}
