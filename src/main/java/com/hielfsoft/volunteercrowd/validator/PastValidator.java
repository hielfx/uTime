package com.hielfsoft.volunteercrowd.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

/**
 * Created by Daniel Sánchez on 25/03/2016.
 */
public class PastValidator implements ConstraintValidator<Past,TemporalAccessor>{
    @Override
    public void initialize(Past constraintAnnotation) {

    }

    @Override
    public boolean isValid(TemporalAccessor value, ConstraintValidatorContext context) {
        if(value==null){
            return true;
        }
        try {
            ZonedDateTime ld = ZonedDateTime.from(value);
            if (ld.isBefore(ZonedDateTime.now())) {
                return true;
            }
        }catch (DateTimeException e){
            LocalDate ld = LocalDate.from(value);
            if(ld.isBefore(LocalDate.now())){
                return true;
            }
        }
        return false;
    }
}
