package com.tericcabrel.authorization.constraints;

import com.tericcabrel.authorization.constraints.validators.IsUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({
    TYPE,
    ANNOTATION_TYPE,
    FIELD
})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUniqueValidator.class)
@Documented
public @interface IsUnique {
    String message() default "{constraints.is-unique}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
    String model();
    String property();

    @Target({
        TYPE,
        ANNOTATION_TYPE,
        FIELD
    })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IsUnique[] value();
    }
}
