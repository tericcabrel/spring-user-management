package com.tericcabrel.authorization.constraints;

import com.tericcabrel.authorization.constraints.validators.IsUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = IsUniqueValidator.class)
@Target({
    TYPE, FIELD,
    ANNOTATION_TYPE
})
@Retention(RUNTIME)
@Documented
public @interface IsUnique {
    String message() default "{constraints.is-unique}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
    String repository();
    String property();

    @Target({
        TYPE, FIELD,
        ANNOTATION_TYPE
    })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IsUnique[] value();
    }
}
