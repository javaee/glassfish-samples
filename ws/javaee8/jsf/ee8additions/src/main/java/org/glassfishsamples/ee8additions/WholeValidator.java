package org.glassfishsamples.ee8additions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WholeValidator implements ConstraintValidator<Whole, WholeBean> {

    @Override
    public void initialize(Whole constraintAnnotation) {
    }

    @Override
    public boolean isValid(WholeBean value, ConstraintValidatorContext context) {
        return false;
    }    
}
