package com.tericcabrel.authorization.constraints;

import com.tericcabrel.authorization.constraints.validators.FieldMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({
    TYPE, FIELD,
    ANNOTATION_TYPE
})
@Retention(RUNTIME)
@Documented
public @interface FieldMatch {
    String message() default "{constraints.field-match}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
    String first();
    String second();

    @Target({
        TYPE, FIELD,
        ANNOTATION_TYPE
    })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
