package com.hielfsoft.volunteercrowd.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Daniel Sánchez on 25/03/2016.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastValidator.class)
@Documented
public @interface Past {

    String message() default "{javax.validation.constraints.Past.message}"; //Default javax Past message

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
