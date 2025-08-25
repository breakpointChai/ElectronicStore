package com.lcwd.electronic.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidatore implements ConstraintValidator<ImageNameValid,String> {

    private Logger logger = LoggerFactory.getLogger(ImageNameValidatore.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        logger.info("message from ImageNameValidatore : {}",s);
        //we write our logic
        if(s.isBlank()){
            return false;
        }
        else {
            return true;
        }


    }
}
