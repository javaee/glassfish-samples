package org.glassfishsamples.ee8additions;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = WholeValidator.class)
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Whole {

    String message() default "Unable to validate bean";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
