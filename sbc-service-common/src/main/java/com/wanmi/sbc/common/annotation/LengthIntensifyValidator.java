//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wanmi.sbc.common.annotation;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;

public class LengthIntensifyValidator implements ConstraintValidator<LengthIntensify, CharSequence> {
    private static final Log log = LoggerFactory.make(MethodHandles.lookup());
    private int min;
    private int max;

    public LengthIntensifyValidator() {
    }

    @Override
    public void initialize(LengthIntensify parameters) {
        this.min = parameters.min();
        this.max = parameters.max();
        this.validateParameters();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) {
            return true;
        } else {
            int length = value.toString().trim().length();
            return length >= this.min && length <= this.max;
        }
    }

    private void validateParameters() {
        if(this.min < 0) {
            throw log.getMinCannotBeNegativeException();
        } else if(this.max < 0) {
            throw log.getMaxCannotBeNegativeException();
        } else if(this.max < this.min) {
            throw log.getLengthCannotBeNegativeException();
        }
    }
}
