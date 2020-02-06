package com.tericcabrel.authorization.constraints;

import com.tericcabrel.authorization.constraints.validators.ExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ExistsValidator.class)
@Target({
    TYPE, FIELD,
    ANNOTATION_TYPE
})
@Retention(RUNTIME)
@Documented
public @interface Exists {
    String message() default "{constraints.exists}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
    String property();
    String repository();

    @Target({
        TYPE, FIELD,
        ANNOTATION_TYPE
    })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Exists[] value();
    }
}
